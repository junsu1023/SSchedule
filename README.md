# SSchedule

친구의 부탁으로 시작된 S사 3교대 근무자를 위한 맞춤형 스케줄 관리 캘린더 앱입니다.

## 📱 주요 기능
- **캘린더**: DAY, SW, GY, OFF 등 다양한 근무 형태를 간편하게 입력하고 한눈에 확인합니다.
- **맞춤형 알림**: 근무 시작 전, 커스텀 레이아웃이 적용된 알림을 통해 당일과 익일의 근무 정보를 제공합니다.
- **홈 화면 위젯**: 앱을 열지 않아도 오늘과 내일의 근무를 홈 화면에서 즉시 확인할 수 있습니다.
- **다크 모드 지원**: 사용자의 눈 피로도를 고려한 시스템 테마 연동 기능을 제공합니다.

## 🛠 기술 스택

| 분류 | 기술 |
| :--- | :--- |
| **Language** | Kotlin |
| **UI** | Jetpack Compose |
| **Architecture** | MVVM, MVI(Unidirectional Data Flow) + Multi-Module |
| **DI** | Hilt |
| **Async / Stream** | Coroutines & Flow |
| **Storage** | Room (Local Database) |
| **Components** | AlarmManager, App Widgets, RemoteViews |
| **AI Collaboration** | Google Gemini |

## 📂 폴더 구조 (Project Structure)

```text
root
├── app                 # UI, ViewModels, UI State 및 Android 전용 로직
│   └── src/main/java/com/example/samsung_work_schedule
│       ├── feature     # 기능별 화면 (Calendar, Setting)
│       ├── navigation  # Compose Navigation 설정
│       ├── notification# 알림 및 알람 관리 (ShiftAlarmManager, Receiver)
│       ├── widget      # 홈 화면 위젯 (AppWidgetProvider)
│       └── theme       # 테마 및 컬러 디자인 시스템
├── domain              # 순수 Kotlin 모듈 (비즈니스 로직)
│   └── src/main/java/com/example/domain
│       ├── model       # 데이터 모델 (WorkSchedule, WorkType)
│       ├── repository  # 인터페이스 정의
│       └── usecase     # 기능별 UseCase (Save, Get, Delete)
└── data                # 데이터 소스 및 저장소 구현체
    └── src/main/java/com/example/data
        ├── repository  # Domain Repository 구현
        ├── local       # Room DB, Entity, DAO
        └── di          # Hilt Data Module (Dependency Injection)
```

## 🤝 AI Collaboration
이 프로젝트는 **Google Gemini AI**를 통해 아키텍처 설계, 버그 수정, 위젯 레이아웃 최적화 등 앱의 전체적인 기능을 완성되었습니다. AI와의 협업을 통해 만들어진 결과물입니다.
