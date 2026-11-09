# Nexus AI v1.0 — مشروع Android جديد

هذا المشروع مستقل تماماً ولا يعتمد على أي ملفات من المشروع السابق.

## ما الموجود في هذه النسخة؟
- تطبيق Android أصلي باستخدام Kotlin وJetpack Compose.
- هوية The Quiet Mind بخلفية OLED سوداء.
- شاشات Chat وResearch وCreative Studio وDeveloper وKnowledge وSettings.
- حفظ رسائل المحادثة محلياً على الجهاز.
- توضيح صريح بأن مزود الذكاء الاصطناعي غير مربوط بعد.
- هيكل أولي لتوسعة المشروع وفق Master Blueprint V5.

## ما الذي لم يُنفذ بعد؟
Puter/provider integration، توليد الصور والفيديو، Infinite Build، الوكلاء، GitHub، Terminal، Error Intelligence، Four-Agent Review، Room project memory، App Factory وعمليات الجهاز. تظهر هذه العناصر كميزات مخططة وليست وظائف عاملة.

## متطلبات البناء
- Android Studio حديث أو بيئة بناء Android متوافقة.
- JDK 17.
- Android SDK Platform 35.
- Gradle 8.9 أو إصدار متوافق مع Android Gradle Plugin 8.7.3.

## البناء
من مجلد NexusAI:

```bash
gradle assembleDebug
```

أو افتح المجلد في Android Studio ثم اختر Build > Build APK(s).

ملف APK الناتج سيكون عادةً:
`app/build/outputs/apk/debug/app-debug.apk`

## ملاحظات
- minSdk 24؛ مناسب من Android 7.0 فما فوق من ناحية الحد الأدنى المعلن.
- لم يتم بناء APK في بيئة إعداد هذه الحزمة لعدم توفر Android SDK وGradle فيها.
- افحص البناء على جهازك قبل تثبيت APK، وأرسل سجل الأخطاء إن ظهر أي خطأ.
