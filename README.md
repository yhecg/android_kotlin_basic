
7/13 월

* 작업 진행 순서
  1. 서버 : 데이터를 전달해줄 서버 구현 ( node.js )
    - Express 기본 환경에서 구현
  [ 이하 안드로이드 ]
  2. 서버에서 데이터를 받을 통신 구현 ( Retrofit )
  3. 서버에서 받은 데이터를 내부에 저장할 Device Database 구현 ( Realm )
    - INSERT, READ, UPDATE, DELETE
  4. 저장된 데이터를 보여줄 화면 구현 ( DataBinding, RecyclerView )

* 적용해야 될 부분
  1. 디자인 패턴 : MVVM
  2. Coroutine
  3. LiveData

* ISSUE
  1. Kotlin 문법
  2. UnitTest ( realm CRUD )
