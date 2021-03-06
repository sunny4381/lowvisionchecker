アクセシビリティ検証・視覚化シミュレーション
===

[みんなのアクセシビリティ評価ツール：miChecker](https://www.soumu.go.jp/main_sosiki/joho_tsusin/b_free/michecker.html)を、コンソールから実行できるようにしたものです。
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

- `lowvisionchecker-$version.jar`: アプリケーション本体
- `libs/` ディレクトリ: 依存する jar を集めたもの。

## アクセシビリティ検証と視覚化

miChecker と同様にアクセシビリティ検証と視覚化とを実行することができます。
この 2 つはサブコマンドとなっており、別々のオプションで実行します。
次節以降で詳しくみていきます。

## アクセシビリティ検証

検証したい URL を指定して、コマンドを次のように実行します。

~~~
$ java -jar target/lowvisionchecker-$version.jar htmlchecke "https://www.yahoo.co.jp/"
~~~

検証に成功すると `a.json` というファイルに検証結果が JSON 形式で出力されます。

### オプション

- --no-browser-headless: ヘッドレスモードを無効にして実行します。既定ではヘッドレスモードで実行します。
- --browser-window-size: ブラウザのサイズを設定します（例: --browser-window-size=1024,768）。既定では自動です。
- --lang: ブラウザの言語を設定します。既定では実行環境の言語を使用します。
- --output-report=検証結果ファイル名: 検証結果ファイル名を指定します。既定では a.json になっています。

## 視覚化

### 実行方法

検証したい URL を指定して、コマンドを次のように実行します。

~~~
$ java -jar target/lowvisionchecker-$version.jar lowvision "https://www.yahoo.co.jp/"
~~~

検証に成功すると `a.json` というファイルに検証結果が JSON 形式で出力されます。

### オプション

- --no-browser-headless: ヘッドレスモードを無効にして実行します。既定ではヘッドレスモードで実行します。
- --browser-window-size: ブラウザのサイズを設定します（例: --browser-window-size=1024,768）。既定では自動です。
- --lang: ブラウザの言語を設定します。既定では実行環境の言語を使用します。
- --output-report=検証結果ファイル名: 検証結果ファイル名を指定します。既定では a.json になっています。
- --output-image=検証結果画像ファイル名: 検証結果画像ファイル名を指定します。既定では検証結果画像は出力しません。
- --source-image=キャプチャ画像ファイル名: 内部ではブラウザに表示された画面を画像化します。その画像化したものをファイルへ出力します。既定では出力しません。
- --no-lowvision-eyesight: 視力を無効にします。既定では有効になっています。
- --lowvision-eyesight-degree: 視力を設定します。既定値は 0.5 です。
- --no-lowvision-cvd: 色覚異常を無効にします。既定では有効になっています。
- --lowvision-cvd-type: 色覚異常種別を設定します。既定では 2 （第二色覚異常）になっています。
- --no-lowvision-color-filter: 水晶体透過率を無効にします。既定では有効になっています。
- --lowvision-color-filter-degree: 水晶体透過率を設定します。既定では 0.8 になっています。

## 特記事項

miChecker には Internet Explorer 11 が組み込まれており、本ツールには Google Chrome が組み込まれています。
このため miChecker の検証結果と本ツールの検証結果が厳密に一致することはありません。

例えば www.facebook.com を両者で検証すると miChecker には多数の「固定サイズのフォントが使われています」という指摘がありますが、
本ツールでは固定サイズのフォントの利用を検出することはできません。
