import { onRequest } from "firebase-functions/v2/https";
import { defineSecret } from "firebase-functions/params";
import { logger } from "firebase-functions/v2";
import * as admin from "firebase-admin";

admin.initializeApp();

const TELEGRAM_BOT_TOKEN = defineSecret("TELEGRAM_BOT_TOKEN");
const TELEGRAM_CHAT_ID = defineSecret("TELEGRAM_CHAT_ID");

const MAX_TEXT_LENGTH = 4000;
const RATE_LIMIT_WINDOW_MS = 60_000;
const RATE_LIMIT_MAP_SOFT_CAP = 5_000;

const ipLastRequest = new Map<string, number>();

function escapeHtml(input: string): string {
    return input
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}

function isRateLimited(ip: string): boolean {
    const now = Date.now();
    const previous = ipLastRequest.get(ip);
    if (previous && now - previous < RATE_LIMIT_WINDOW_MS) {
        return true;
    }
    ipLastRequest.set(ip, now);
    if (ipLastRequest.size > RATE_LIMIT_MAP_SOFT_CAP) {
        for (const [key, ts] of ipLastRequest.entries()) {
            if (now - ts > RATE_LIMIT_WINDOW_MS) {
                ipLastRequest.delete(key);
            }
        }
    }
    return false;
}

interface FeedbackBody {
    text?: unknown;
    appVersion?: unknown;
    deviceModel?: unknown;
    androidSdk?: unknown;
    locale?: unknown;
    isPgsSignedIn?: unknown;
}

function asString(value: unknown, fallback: string = "?"): string {
    return typeof value === "string" && value.length > 0 ? value : fallback;
}

function asNumber(value: unknown): number {
    return typeof value === "number" && Number.isFinite(value) ? value : 0;
}

function asBoolean(value: unknown): boolean {
    return value === true;
}

function buildMessage(body: FeedbackBody, text: string): string {
    const appVersion = escapeHtml(asString(body.appVersion));
    const deviceModel = escapeHtml(asString(body.deviceModel));
    const androidSdk = asNumber(body.androidSdk);
    const locale = escapeHtml(asString(body.locale));
    const isPgsSignedIn = asBoolean(body.isPgsSignedIn);
    return [
        "<b>📩 Sudoku Feedback</b>",
        "",
        escapeHtml(text),
        "",
        "<b>Context</b>",
        `App: <code>${appVersion}</code>`,
        `Device: <code>${deviceModel}</code>`,
        `SDK: <code>${androidSdk}</code>`,
        `Locale: <code>${locale}</code>`,
        `PGS: <code>${isPgsSignedIn}</code>`,
    ].join("\n");
}

async function sendToTelegram(token: string, chatId: string, message: string): Promise<void> {
    const response = await fetch(`https://api.telegram.org/bot${token}/sendMessage`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            chat_id: chatId,
            text: message,
            parse_mode: "HTML",
            disable_web_page_preview: true,
        }),
    });
    if (!response.ok) {
        const errorBody = await response.text();
        throw new Error(`Telegram API ${response.status}: ${errorBody}`);
    }
}

export const submitFeedback = onRequest(
    {
        region: "europe-west1",
        secrets: [TELEGRAM_BOT_TOKEN, TELEGRAM_CHAT_ID],
        memory: "256MiB",
        cpu: 1,
        timeoutSeconds: 30,
        maxInstances: 10,
        invoker: "public",
    },
    async (req, res) => {
        if (req.method !== "POST") {
            res.status(405).send("Method Not Allowed");
            return;
        }

        const ip = req.ip ?? "unknown";
        if (isRateLimited(ip)) {
            logger.warn("Rate limit exceeded", { ip });
            res.status(429).send("Too Many Requests");
            return;
        }

        const body = (req.body ?? {}) as FeedbackBody;
        const rawText = typeof body.text === "string" ? body.text.trim() : "";
        if (rawText.length === 0 || rawText.length > MAX_TEXT_LENGTH) {
            res.status(400).send("Invalid text");
            return;
        }

        const message = buildMessage(body, rawText);

        try {
            await sendToTelegram(TELEGRAM_BOT_TOKEN.value(), TELEGRAM_CHAT_ID.value(), message);
            res.status(200).send("OK");
        } catch (err) {
            logger.error("submitFeedback failed", err);
            res.status(502).send("Upstream Error");
        }
    }
);

const LEADERBOARD_RATE_WINDOW_MS = 60_000;
const MAX_SCORE_DELTA = 2000;
const MAX_DISPLAY_NAME_LENGTH = 32;
const STABLE_ID_PATTERN = /^(pgs_|dev_)[A-Za-z0-9_-]{1,64}$/;
const PLATFORMS = new Set(["android", "ios"]);

const leaderboardRateMap = new Map<string, number>();

function sanitizeName(raw: unknown): string {
    if (typeof raw !== "string" || raw.trim().length === 0) return "Anonymous";
    return raw
        .trim()
        .replace(/[<>&"]/g, "")
        .slice(0, MAX_DISPLAY_NAME_LENGTH)
        .trim() || "Anonymous";
}

function isLeaderboardRateLimited(key: string): boolean {
    const now = Date.now();
    const prev = leaderboardRateMap.get(key);
    if (prev && now - prev < LEADERBOARD_RATE_WINDOW_MS) return true;
    leaderboardRateMap.set(key, now);
    if (leaderboardRateMap.size > 5_000) {
        for (const [k, ts] of leaderboardRateMap.entries()) {
            if (now - ts > LEADERBOARD_RATE_WINDOW_MS) leaderboardRateMap.delete(k);
        }
    }
    return false;
}

interface LeaderboardBody {
    stableId?: unknown;
    platform?: unknown;
    displayName?: unknown;
    avatarUrl?: unknown;
    scoreDelta?: unknown;
    gameContext?: unknown;
}

interface GameContext {
    difficulty?: unknown;
    timeSeconds?: unknown;
}

function maxScoreForContext(ctx: GameContext): number {
    const difficulty = typeof ctx.difficulty === "number" ? ctx.difficulty : -1;
    const time = typeof ctx.timeSeconds === "number" ? ctx.timeSeconds : 0;
    if (time <= 0) return 0;
    const targetTime = difficulty === 0 ? 300 : difficulty === 1 ? 600 : 1200;
    const base = difficulty === 0 ? 100 : difficulty === 1 ? 250 : 500;
    const speed = Math.min(targetTime / time, 2.0);
    return Math.ceil(base * speed * 1.3 * 1.5);
}

export const submitLeaderboard = onRequest(
    {
        region: "europe-west1",
        memory: "256MiB",
        cpu: 1,
        timeoutSeconds: 30,
        maxInstances: 10,
        invoker: "public",
    },
    async (req, res) => {
        if (req.method !== "POST") {
            res.status(405).send("Method Not Allowed");
            return;
        }

        const body = (req.body ?? {}) as LeaderboardBody;

        const stableId = typeof body.stableId === "string" ? body.stableId.trim() : "";
        if (!STABLE_ID_PATTERN.test(stableId)) {
            res.status(400).send("Invalid stableId");
            return;
        }

        const platform = typeof body.platform === "string" ? body.platform : "";
        if (!PLATFORMS.has(platform)) {
            res.status(400).send("Invalid platform");
            return;
        }

        const scoreDelta = typeof body.scoreDelta === "number" && Number.isFinite(body.scoreDelta)
            ? Math.floor(body.scoreDelta)
            : 0;
        if (scoreDelta <= 0) {
            res.status(400).send("Invalid scoreDelta");
            return;
        }

        const ip = req.ip ?? "unknown";
        const rateLimitKey = `${stableId}:${ip}`;
        if (isLeaderboardRateLimited(rateLimitKey)) {
            res.status(429).send("Too Many Requests");
            return;
        }

        const ctx = (typeof body.gameContext === "object" && body.gameContext !== null
            ? body.gameContext : {}) as GameContext;
        const maxAllowed = Math.min(maxScoreForContext(ctx), MAX_SCORE_DELTA);
        const clampedDelta = Math.min(scoreDelta, maxAllowed);
        if (clampedDelta <= 0) {
            res.status(400).send("Score out of plausible range");
            return;
        }

        const displayName = sanitizeName(body.displayName);
        const avatarUrl = typeof body.avatarUrl === "string" && body.avatarUrl.startsWith("http")
            ? body.avatarUrl
            : null;

        const ref = admin.database().ref(`/leaderboard/${stableId}`);

        try {
            await ref.transaction((current: { score?: number; platform?: string; displayName?: string; avatarUrl?: string | null; updatedAt?: number } | null) => {
                const prev = current ?? { score: 0 };
                return {
                    platform,
                    displayName,
                    avatarUrl,
                    score: (prev.score ?? 0) + clampedDelta,
                    updatedAt: Date.now(),
                };
            });
            res.status(200).send("OK");
        } catch (err) {
            logger.error("submitLeaderboard failed", err);
            res.status(502).send("Upstream Error");
        }
    }
);
