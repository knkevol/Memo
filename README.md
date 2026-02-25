# 📝 *MemoApp Project*
Kotlin 기반 Todo, 메모앱
<br />
<br />
<br />
***

### ✅ 주요 기능
- **전체 화면 구성**
    
   [MainActivity]
  - 하단 BottomNavigationView 3개 탭 관리
  - 탭 구성 : Chat(채팅) / Folder(폴더) / Setting(설정)
  - Fragment 교체 방식으로 화면 전환
 <br />
 
  | 하단 탭별 Fragment | 내용 |
  |------|------|
  | ChatFragment | 채팅 / 할 일 목록 화면 |
  | FolderListFragment | 폴더 목록 화면 |
  | SettingFragment | 설정 화면 |
  
- **Todo 메모 기능**
    
   [ChatFragment]
  - 텍스트 입력 후 전송 버튼으로 채팅 메시지 추가
  - ListView에 메시지 실시간 표시 (LiveData 관찰)
  - 항목 클릭 시 해당 메시지를 "보관함(ChatClear)"으로 이동
  - "Clear" 버튼 : 전체 메시지를 보관함으로 이동 후 화면 전환
 
   [ChatClearFragment]
  - 이동된 메시지 목록 표시
  - 항목 클릭 시 영구 삭제
  - 뒤로가기 버튼으로 ChatFragment 복귀
 
- **폴더 관리 기능 (Folder)**
    
   [FolderListFragment]]
  - 2열 StaggeredGridLayout으로 폴더 카드 표시
  - FAB(+) 버튼으로 새 폴더 생성 화면 이동
  - 각 폴더 카드 우측 메뉴(PopupMenu) : "제목 수정" / "삭제"
  - SearchView로 폴더 이름 실시간 필터링
  - 폴더 탭 시 해당 폴더의 메모 목록(MemoListFragment)으로 이동
 
    [FolderFragment]
  - 폴더 이름 입력 후 OK 버튼으로 저장
  - 생성/수정 모드 공통 사용

   [EditFolderFragment]
  - 기존 폴더 이름 사전 입력 후 수정 저장

   [폴더 삭제]
  - 폴더 삭제 시 해당 폴더 소속 메모 전체 CASCADE 삭제
  - FolderFragmentListener 인터페이스를 통해 MainActivity로 이벤트 전달
 
- **메모 관리 기능 (Memo / Note)**
    
    [MemoListFragment]
  - 특정 폴더의 메모만 필터링하여 2열 그리드 표시
  - FAB(+) 버튼으로 새 메모 작성 화면 이동
  - SearchView로 메모 제목 실시간 필터링
  - 메모 카드 탭 시 수정 화면(MemoFragment)으로 이동
  - 화면 진입 시 하단 네비게이션 바 자동 숨김

   [MemoFragment]
  - 제목(Title), 부제목(SubTitle), 본문(NoteText) 입력
  - 작성 일시 자동 스탬핑
  - 마크다운(Markdown) 텍스트 지원
  - 하단 시트(BottomSheet) 메뉴로 부가 기능 접근

   [메모 색상 지정]
  - 7가지 색상 테마 선택 가능 (파랑, 노랑, 보라, 초록, 주황, 검정 등)
  - 선택 색상이 메모 카드 배경색에 즉시 반영
  - 색상값은 HEX 코드로 DB에 저장

   [이미지 첨부] (구현중)
  - 갤러리에서 이미지 선택 (READ_EXTERNAL_STORAGE 권한 요청)
  - 이미지 파일 경로를 DB에 저장, BitmapFactory로 카드에 미리보기 표시
  - EasyPermissions 라이브러리로 런타임 권한 처리

   [웹 링크 첨부] (구현중)
  - URL 입력 필드로 웹 링크 추가
  - URL 유효성 검사 후 저장
  - 메모 카드에서 클릭 가능한 링크로 표시

   [메모 삭제]
  - 수정 화면 하단 시트에서 "삭제" 선택으로 제거
  - 삭제 후 메모 목록으로 복귀

   [NotesBottomSheetFragment]
  - 색상 선택 / 이미지 추가 / 웹 URL 입력 / 메모 삭제 옵션 제공
  - LocalBroadcastManager로 선택 결과를 MemoFragment에 전달
 
- **검색 기능**
  - 폴더 목록 화면 : 폴더 이름으로 실시간 검색 (대소문자 무관)
  - 메모 목록 화면 : 메모 제목으로 실시간 검색 (대소문자 무관)
  - SearchView 입력 즉시 어댑터 필터링 적용


<br />

***

<br />

### 🏢 데이터베이스 구조

<br />


  | DB 이름 | 테이블 | 주요 컬럼 |
  |------|------|------|
  | notes_database | Notes | id, title, subTitle, dateTime, noteText, imgPath, webLink, color, folderId |
  | folders.db (v2) | Folders | id, title |
  | chat_database | chats | id, text |
  | chat_clear_database | chat_clear | id, text |
