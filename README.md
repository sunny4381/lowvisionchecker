ロービジョン シミュレーション
===

[みんなのアクセシビリティ評価ツール：miChecker](https://www.soumu.go.jp/main_sosiki/joho_tsusin/b_free/michecker.html)のロービジョン シミュレーションを、コンソールから実行できるようにしたものです。
Windows だけでなく、Mac や Linux でも動作します。

## 前提条件

Google Chrome, Java 8 と Maven が必要です。事前にインストールしておいてください。

Java 9 以上での動作は確認していません。

## ビルド方法

次のコマンドを実行します。

~~~
$ mvn clean package
~~~

ビルドに成功すると `target` ディレクトリに次のものが作成されます。

- `lowvisionchecker-$version.jar`: ロービジョン シミュレーションの本体
- `libs/` ディレクトリ: 依存する jar を集めたもの。

## 実行方法

検証したい URL を指定して、コマンドを次のように実行します。

~~~
$ java -jar target/lowvisionchecker-$version.jar "https://www.yahoo.co.jp/"
~~~

検証に成功すると `a.json` というファイルに検証結果が JSON 形式で出力されます。

### オプション

- --no-headless: ヘッドレスモードを無効にして実行します。既定ではヘッドレスモードで実行します。
- --output=出力ファイル名: 出力ファイル名を指定します。既定では a.json になっています。
- --no-lowvision-eyesight: 視力を無効にします。既定では有効になっています。
- --lowvision-eyesight-degree: 視力を設定します。既定値は 0.5 です。
- --no-lowvision-cvd: 色覚異常を無効にします。既定では有効になっています。
- --lowvision-cvd-type: 色覚異常種別を設定します。既定では 2 （第二色覚異常）になっています。
- --no-lowvision-color-filter: 水晶体透過率を無効にします。既定では有効になっています。
- --lowvision-color-filter-degree: 水晶体透過率を設定します。既定では 0.8 になっています。

## 特記事項

miChecker には Internet Explorer 11？ が組み込まれており、本ツールには Google Chrome を組み込んでいます。
このため miChecker の検証結果と本ツールの検証結果が厳密に一致することはありません。

例えば www.facebook.com を両者で検証すると miChecker には多数の「固定サイズのフォントが使われています」という指摘がありますが、本ツールにはありません。
