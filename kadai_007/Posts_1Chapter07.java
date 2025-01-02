package kadai_007;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Posts_1Chapter07 {
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement preparedStatement = null;
        
        // 投稿データ
        String[][] postedData = {
        		{ "1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13" },
        		{ "1002", "2023-02-08", "お疲れ様です！", "12" },
        		{ "1003", "2023-02-09", "今日も頑張ります！", "18" },
        		{ "1001", "2023-02-09", "無理は禁物ですよ！", "17" },
        		{ "1002", "2023-02-10", "明日から連休ですね！", "20" }
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "f2AQLRTEh-VvUq"
            );

            System.out.println( "データベース接続成功:" + con );
            
            //　追加をするSQLクエリを準備
         	String insertSql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
         	preparedStatement = con.prepareStatement(insertSql);
         	
         	// リストの１行目から順番に読み込む
         	int rowCnt = 0;
         	for( int i = 0; i < postedData.length; i++ ) {
         		// SQLクエリの「？」部分をリストのデータに置き換え
         		preparedStatement.setString(1, postedData[i][0]); // ユーザーID
         		preparedStatement.setString(2, postedData[i][1]); // 投稿日時
         		preparedStatement.setString(3, postedData[i][2]); // 投稿内容
         		preparedStatement.setString(4, postedData[i][3]); // いいね数
         		
         		// executeUpdate()の戻り値を利用して件数をインクリメント
         		rowCnt += preparedStatement.executeUpdate();
         	}
         	// SQLクエリを実行 （DBMSに送信）
         	System.out.println( "レコード追加を実行します" );
         	System.out.println( rowCnt + "件のレコードが追加されました");
         	
         	// 取得するSQLクエリを準備
         	Statement statement = con.createStatement();
            String selectSql = "SELECT posted_at, post_content, likes FROM posts WHERE user_id = 1002;";

            //　SQLクエリを実行（DBMSに送信）
            ResultSet result = statement.executeQuery(selectSql);
            System.out.println("ユーザーIDが1002のレコードを検索しました");

            // SQLクエリの実行結果を抽出
            while(result.next()) {
                Date postedAt = result.getDate("posted_at");
                String postContent = result.getString("post_content");
                int likes = result.getInt("likes");
                // 結果の表示
                System.out.println(result.getRow() + "件目:投稿日時＝" + postedAt + "／投稿内容＝" + postContent + "／いいね数＝" + likes );
            }
            
        } catch(SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            // 使用したオブジェクトを解放
            if( preparedStatement != null ) {
                try { preparedStatement.close(); } catch(SQLException ignore) {}
            }
            if( con != null ) {
                try { con.close(); } catch(SQLException ignore) {}
            }
        }
    }
}
