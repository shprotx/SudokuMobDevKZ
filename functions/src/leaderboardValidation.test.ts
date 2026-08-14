import { test } from "node:test";
import assert from "node:assert/strict";
import { ACHIEVEMENTS_COUNT_MAX, sanitizeAchievementsCount } from "./leaderboardValidation.js";

test("sanitizeAchievementsCount accepts zero", () => {
    assert.equal(sanitizeAchievementsCount(0), 0);
});

test("sanitizeAchievementsCount accepts a mid-range integer", () => {
    assert.equal(sanitizeAchievementsCount(17), 17);
});

test("sanitizeAchievementsCount accepts the ceiling value", () => {
    assert.equal(sanitizeAchievementsCount(ACHIEVEMENTS_COUNT_MAX), ACHIEVEMENTS_COUNT_MAX);
});

test("sanitizeAchievementsCount rejects a value above the ceiling", () => {
    assert.equal(sanitizeAchievementsCount(ACHIEVEMENTS_COUNT_MAX + 1), undefined);
});

test("sanitizeAchievementsCount rejects negative values", () => {
    assert.equal(sanitizeAchievementsCount(-1), undefined);
});

test("sanitizeAchievementsCount rejects non-integer numbers", () => {
    assert.equal(sanitizeAchievementsCount(4.5), undefined);
});

test("sanitizeAchievementsCount rejects NaN and Infinity", () => {
    assert.equal(sanitizeAchievementsCount(Number.NaN), undefined);
    assert.equal(sanitizeAchievementsCount(Number.POSITIVE_INFINITY), undefined);
});

test("sanitizeAchievementsCount rejects non-number types", () => {
    assert.equal(sanitizeAchievementsCount("10"), undefined);
    assert.equal(sanitizeAchievementsCount(null), undefined);
    assert.equal(sanitizeAchievementsCount(undefined), undefined);
    assert.equal(sanitizeAchievementsCount(true), undefined);
    assert.equal(sanitizeAchievementsCount({ count: 5 }), undefined);
});

test("sanitizeAchievementsCount treats a missing field as valid absence", () => {
    const body: { achievementsCount?: unknown } = {};
    assert.equal(sanitizeAchievementsCount(body.achievementsCount), undefined);
});
