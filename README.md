# 画面設計
![image](https://github.com/user-attachments/assets/86e924f5-25f1-437b-a9be-67830e077c91)


# gitの操作一覧

## masterブランチから最新情報を取得する
> git pull

最新のブランチ情報にした後に新しいブランチを作成しなければ、最悪の場合コンフリクト(衝突)が発生します

## branch作成
> git checkout -b ブランチ名

-b オプションを付けることで、作成したブランチにそのまま移動することができます
なお、現在のブランチを調べる場合は

> git branch -a

で調べてください

## branch移動
> git checkout ブランチ名

## 変更したファイルを追加
> git add --all

## 変更したファイルをコミット
> git commit -m "コミットメッセージを入れる"

コミットメッセージは、現段階の変更状況を入れてください
例 feat:ボタンを追加

## コミットをプッシュする
> git push origin 現在作成中のブランチ名

例 現在作業中のブランチ名：feat/create_button | プッシュする場合：git push origin feat/create_button

## コンフリクト発生時
> git checkout master
> 
> git pull
> 
> git checkout 作業ブランチ
> 
> git rebase master



プルリクエスト作成後、レビューが必要であればレビュアーにYutoFuseを追加しておいてください
また、必要ない場合、マージ時には "rebase and merge" を選択してください

分からないことがあればissueに
