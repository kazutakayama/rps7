package com.example.rps7.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rps7")
public class Rps7RestController {

    // プレイヤーが勝利する確率のパーセンテージを0~100の整数で指定
    private final int winPercentage = 40;
    // プレイヤーが敗北する確率のパーセンテージを0~100の整数で指定 ※winPercentageとlosePercentageの和を100以下にする
    private final int losePercentage = 40;

    private final Random random = new Random();

    // じゃんけんの手の配列
    private final String[] hands = { "グー", "チョキ", "パー", "ウォーター", "エア", "スポンジ", "ファイア" };
    // じゃんけんの勝つパターンをマップにする
    private final Map<Integer, List<Integer>> winMap = new HashMap<>();
    // じゃんけんの負けるパターンをマップにする 中身はコンストラクタとloseMapメソッドで初期化する
    private final Map<Integer, List<Integer>> loseMap = new HashMap<>();

    // コンストラクタ 勝敗のパターンのマップを初期化する
    public Rps7RestController() {
        initWinMap();
        initLoseMap();
    }

    // プレイヤーの手を0~6の整数で受け取る
    @PostMapping
    public Map<String, Object> play(@RequestParam("hand") int hand) {

        // 0~6以外の整数を受け取ったら、エラーを返す
        if (hand < 0 || hand >= hands.length) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "handは0~6の整数で指定してください");
            return error;
        }

        Map<String, Object> response = new LinkedHashMap<>();

        // 0~99のランダムな整数を生成
        int number = random.nextInt(100);
        // コンピューターの手
        int cp;
        // 結果
        String result;

        // プレイヤーの勝利
        if (number < winPercentage) {
            List<Integer> list = winMap.get(hand);
            cp = list.get(random.nextInt(list.size()));
            result = "勝利";
            // プレイヤーの敗北
        } else if (number < winPercentage + losePercentage) {
            List<Integer> list = loseMap.get(hand);
            cp = list.get(random.nextInt(list.size()));
            result = "敗北";
            // あいこ
        } else {
            cp = hand;
            result = "あいこ";
        }

        response.put("あなた", hands[hand]);
        response.put("コンピュータ", hands[cp]);
        response.put("結果", result);

        return response;
    }

    // 勝利パターン（それぞれの手をkeyに、その手が勝つ手のリストをvalueにする）
    private void initWinMap() {
        winMap.put(0, Arrays.asList(1, 5, 6)); // グー
        winMap.put(1, Arrays.asList(2, 4, 5)); // チョキ
        winMap.put(2, Arrays.asList(0, 3, 4)); // パー
        winMap.put(3, Arrays.asList(0, 1, 6)); // ウォーター
        winMap.put(4, Arrays.asList(0, 3, 6)); // エア
        winMap.put(5, Arrays.asList(2, 3, 4)); // スポンジ
        winMap.put(6, Arrays.asList(1, 2, 5)); // ファイア
    }

    // 敗北パターン（それぞれの手をkeyに、その手が負ける手のリストをvalueにする）
    private void initLoseMap() {
        loseMap.put(0, Arrays.asList(2, 3, 4)); // グー
        loseMap.put(1, Arrays.asList(0, 3, 6)); // チョキ
        loseMap.put(2, Arrays.asList(1, 5, 6)); // パー
        loseMap.put(3, Arrays.asList(2, 4, 5)); // ウォーター
        loseMap.put(4, Arrays.asList(1, 2, 5)); // エア
        loseMap.put(5, Arrays.asList(0, 1, 6)); // スポンジ
        loseMap.put(6, Arrays.asList(0, 3, 4)); // ファイア
    }
}
