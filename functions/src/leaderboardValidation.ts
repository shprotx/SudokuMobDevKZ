export const ACHIEVEMENTS_COUNT_MAX = 50;

export function sanitizeAchievementsCount(value: unknown): number | undefined {
    if (typeof value !== "number" || !Number.isFinite(value)) return undefined;
    if (!Number.isInteger(value)) return undefined;
    if (value < 0 || value > ACHIEVEMENTS_COUNT_MAX) return undefined;
    return value;
}
