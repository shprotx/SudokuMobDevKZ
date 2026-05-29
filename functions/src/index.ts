import { onRequest } from "firebase-functions/v2/https";
import { defineSecret } from "firebase-functions/params";
import { logger } from "firebase-functions/v2";

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
