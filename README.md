◇コード構造
MainActivity
┗MainLayout
 ┗HomeScreen
 ┗DataScreen
  ┗ResultData

◇各コードの説明
☆MainActivity
　アプリケーションのエントリーポイントで、Compose UI を設定して表示するファイル
☆MainLayout
　アプリのメイン画面を構成し、ナビゲーションやレイアウトを管理するファイル
☆HomeScreen
　時刻の入力と検索、結果表示、及び検索結果の保存を行うCompose UIの画面と関連する関数があるファイル
☆DataScreen
 検索結果を一覧表示するためのファイル
☆ResultData
 検索結果を保存取得するためのRoomデータベースのエンティティ、DAO、データベースクラスを定義しているファイル
