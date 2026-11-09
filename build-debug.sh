#!/usr/bin/env bash
set -euo pipefail
if ! command -v gradle >/dev/null 2>&1; then
  echo "Gradle غير موجود. افتح المشروع في Android Studio أو ثبّت Gradle 8.9+ وJDK 17."
  exit 1
fi
gradle --version
gradle assembleDebug
APK="app/build/outputs/apk/debug/app-debug.apk"
if [[ -f "$APK" ]]; then
  echo "نجح البناء: $APK"
else
  echo "انتهى Gradle لكن لم يتم العثور على APK في المسار المتوقع."
  exit 2
fi
