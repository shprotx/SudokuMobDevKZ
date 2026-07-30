#!/usr/bin/env python3
"""Заливка новых ачивок в Play Games Services (draft) через Games Configuration API.

Использование:
  venv/bin/python scripts/pgs_upload_achievements.py --list
  venv/bin/python scripts/pgs_upload_achievements.py --upload

Сервис-аккаунт берётся из PLAY_SERVICE_ACCOUNT_FILE в local.properties.
После заливки ачивки остаются в draft — публикация руками в Play Console.
"""

import argparse
import re
import sys
import defusedxml.ElementTree as ET
from pathlib import Path

from google.oauth2 import service_account
from googleapiclient.discovery import build

ROOT = Path(__file__).resolve().parent.parent
APP_ID = "847632350121"
ICONS_DIR = ROOT / "marketing" / "achievement-icons-v2"

# id -> (iconKey, initialState, sortRank)
NEW_ACHIEVEMENTS = {
    "wins_100": ("trophy_silver", "REVEALED", 106),
    "wins_300": ("trophy_gold", "REVEALED", 107),
    "wins_750": ("trophy_platinum", "REVEALED", 108),
    "perfect_25": ("diamond_pink", "REVEALED", 109),
    "perfect_100": ("diamond_ring", "REVEALED", 110),
    "streak_50": ("fire_blue", "REVEALED", 111),
    "daily_streak_14": ("calendar_fortnight", "REVEALED", 112),
    "daily_25": ("calendar_stack", "REVEALED", 113),
    "daily_100": ("hourglass_gold", "REVEALED", 114),
    "diff_universal_50": ("globe", "REVEALED", 115),
    "marathon_10h": ("stopwatch", "REVEALED", 116),
    "speed_elite_easy": ("comet_green", "REVEALED", 117),
    "speed_elite_medium": ("comet_orange", "REVEALED", 118),
    "speed_elite_hard": ("comet_purple", "REVEALED", 119),
    "no_hints_25": ("owl", "REVEALED", 120),
    "secret_on_the_edge": ("shield_cracked", "HIDDEN", 121),
}


def load_strings(locale_dir):
    path = ROOT / "app/src/main/res" / locale_dir / "strings.xml"
    tree = ET.parse(path)
    return {el.get("name"): el.text or "" for el in tree.getroot().iter("string")}


def texts_for(achievement_id, en, ru):
    key = f"achievement_{achievement_id}"
    return {
        "title_en": en[f"{key}_title"],
        "desc_en": en[f"{key}_desc"],
        "title_ru": ru[f"{key}_title"],
        "desc_ru": ru[f"{key}_desc"],
    }


def localized(en_value, ru_value):
    return {
        "translations": [
            {"locale": "en-US", "value": en_value},
            {"locale": "ru-RU", "value": ru_value},
        ]
    }


def get_service():
    props = (ROOT / "local.properties").read_text()
    match = re.search(r"^PLAY_SERVICE_ACCOUNT_FILE=(.+)$", props, re.M)
    if not match:
        sys.exit("PLAY_SERVICE_ACCOUNT_FILE не найден в local.properties")
    creds = service_account.Credentials.from_service_account_file(
        match.group(1).strip(),
        scopes=["https://www.googleapis.com/auth/androidpublisher"],
    )
    return build("gamesConfiguration", "v1configuration", credentials=creds)


def list_achievements(service):
    resp = service.achievementConfigurations().list(applicationId=APP_ID, maxResults=100).execute()
    items = resp.get("items", [])
    print(f"Всего в PGS: {len(items)}")
    for item in items:
        name = item["draft"]["name"]["translations"][0]["value"]
        print(f"  {item['id']}  {item.get('initialState', '?'):8}  {name}")
    return items


def upload(service):
    en = load_strings("values")
    ru = load_strings("values-ru")
    existing = {
        item["draft"]["name"]["translations"][0]["value"]
        for item in service.achievementConfigurations()
        .list(applicationId=APP_ID, maxResults=100)
        .execute()
        .get("items", [])
    }

    results = {}
    for ach_id, (icon_key, initial_state, sort_rank) in NEW_ACHIEVEMENTS.items():
        t = texts_for(ach_id, en, ru)
        if t["title_en"] in existing:
            print(f"SKIP {ach_id}: '{t['title_en']}' уже есть в PGS")
            continue

        body = {
            "achievementType": "STANDARD",
            "initialState": initial_state,
            "draft": {
                "name": localized(t["title_en"], t["title_ru"]),
                "description": localized(t["desc_en"], t["desc_ru"]),
                "pointValue": 5,
                "sortRank": sort_rank,
            },
        }
        created = service.achievementConfigurations().insert(applicationId=APP_ID, body=body).execute()
        pgs_id = created["id"]
        results[ach_id] = pgs_id
        print(f"OK   {ach_id}: {pgs_id}  '{t['title_en']}' (иконку загрузить руками в консоли: {icon_key}.png)")

    print("\npgsId для реестра:")
    for ach_id, pgs_id in results.items():
        print(f'    "{ach_id}" -> "{pgs_id}"')


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--list", action="store_true")
    parser.add_argument("--upload", action="store_true")
    args = parser.parse_args()

    service = get_service()
    if args.upload:
        upload(service)
    else:
        list_achievements(service)


if __name__ == "__main__":
    main()