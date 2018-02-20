package com.monke.monkeybook.help;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monke.monkeybook.bean.BookShelfBean;
import com.monke.monkeybook.bean.BookSourceBean;
import com.monke.monkeybook.bean.ReplaceRuleBean;
import com.monke.monkeybook.bean.SearchHistoryBean;
import com.monke.monkeybook.dao.DbHelper;
import com.monke.monkeybook.model.BookSourceManage;

import java.io.File;
import java.util.List;

/**
 * Created by GKF on 2018/1/30.
 */

public class DataRestore  {

    public static DataRestore getInstance() {
        return new DataRestore();
    }

    public Boolean run() throws Exception{
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (file != null) {
            restoreBookSource(file);
            restoreReplaceRule(file);
            restoreBookShelf(file);
            restoreSearchHistory(file);
            return true;
        }
        return false;
    }

    private void restoreBookShelf(File file) throws Exception{
        String json = FileHelper.readString("myBookShelf.xml", file.getPath());
        if (json != null) {
            List<BookShelfBean> bookShelfList = new Gson().fromJson(json, new TypeToken<List<BookShelfBean>>() {
            }.getType());
            for (BookShelfBean bookshelf : bookShelfList) {
                DbHelper.getInstance().getmDaoSession().getBookShelfBeanDao().insertOrReplace(bookshelf);
                DbHelper.getInstance().getmDaoSession().getBookInfoBeanDao().insertOrReplace(bookshelf.getBookInfoBean());
            }
        }
    }

    private void restoreBookSource(File file) throws Exception{
        String json = FileHelper.readString("myBookSource.xml", file.getPath());
        if (json != null) {
            List<BookSourceBean> bookSourceBeans = new Gson().fromJson(json, new TypeToken<List<BookSourceBean>>() {
            }.getType());
            BookSourceManage.addBookSource(bookSourceBeans);
        }
    }

    private void restoreSearchHistory(File file) throws Exception{
        String json = FileHelper.readString("myBookSearchHistory.xml", file.getPath());
        if (json != null) {
            List<SearchHistoryBean> searchHistoryBeans = new Gson().fromJson(json, new TypeToken<List<SearchHistoryBean>>() {
            }.getType());
            if (searchHistoryBeans != null && searchHistoryBeans.size() > 0) {
                DbHelper.getInstance().getmDaoSession().getSearchHistoryBeanDao().insertOrReplaceInTx(searchHistoryBeans);
            }

        }
    }

    private void restoreReplaceRule(File file) throws Exception {
        String json = FileHelper.readString("myBookReplaceRule.xml", file.getPath());
        if (json != null) {
            List<ReplaceRuleBean> replaceRuleBeans = new Gson().fromJson(json, new TypeToken<List<ReplaceRuleBean>>() {
            }.getType());
            if (replaceRuleBeans != null && replaceRuleBeans.size() > 0) {
                DbHelper.getInstance().getmDaoSession().getReplaceRuleBeanDao().insertOrReplaceInTx(replaceRuleBeans);
            }
        }
    }
}
