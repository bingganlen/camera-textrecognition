package com.qgqaqgqa.deviceinfomanager.model;

import java.io.Serializable;
import java.util.List;

/**
 * User: Created by 钱昱凯
 * Date: 2019/12/22
 * Time: 23:37
 * EMail: 342744291@qq.com
 */
public class SearchModel implements Serializable {

    /**
     * log_id : 763294633165866390
     * direction : 0
     * words_result_num : 21
     * words_result : [{"words":"K镇线315四"},{"words":"成太线02号(315(2)变压器"},{"words":"成镇线19号(正阳315台区"},{"words":"成镇线-18号(阳100KVA台"},{"words":"成镇线15号新站七组变压器)"},{"words":"成镇线们1号(一道街北160KVA变台"},{"words":"成镇线-10号(三道街北变压器"},{"words":"成镇线-115"},{"words":"道街南变压器"},{"words":"成镇线-08号(红光三组变压器"},{"words":"成镇线-09号(渔池变压器"},{"words":"10kV成镇线913(二道街南变台50KVA"},{"words":"成镇线07号(红光二组变压器)"},{"words":"成镇线-16号(新站五组变压器)"},{"words":"成镇线04号(穗红变压器)"},{"words":"成镇线02号(二道街北315KVA变台)"},{"words":"成镇线-14号(新站变压器"},{"words":"10KV成镇线供电所家属变压器"},{"words":"∨成镇线二道街北100KVA变压器"},{"words":"10kV成镇线四道街南变台"},{"words":"0k繁大线监狱三中队"}]
     */

    private long log_id;
    private int direction;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<WordsResultBean> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResultBean> words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * words : K镇线315四
         */

        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }
}
