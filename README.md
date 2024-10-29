## gitの操作一覧

# branch作成
git checkout -b ブランチ名

# branch移動
# git checkout ブランチ名

# 変更したファイルを追加
git add --all

# 変更したファイルをコミット
git commit -m "コミットメッセージを入れる"

# コミットをプッシュする
git push origin 現在作成中のブランチ名
例：
　現在作業中のブランチ名：feat/create_button
　プッシュする場合　　　：git push origin feat/create_button

プルリクエスト作成後、レビューが必要であればレビュアーにYutoFuseを追加しておいてください
また、必要ない場合、マージ時には "rebase and merge" を選択してください
