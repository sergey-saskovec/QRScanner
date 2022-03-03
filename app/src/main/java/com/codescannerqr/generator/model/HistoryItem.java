package com.codescannerqr.generator.model;

import com.google.zxing.client.result.ParsedResult;

public class HistoryItem {

    private final String titleHistory;
    private String urlHistory;
    private final int iconHistory;
    private final byte[] arrayQRCode;

    public HistoryItem(String titleHistory, String urlHistory, int iconHistory,
                       byte[] arrayQRCode) {
        this.titleHistory = titleHistory;
        this.urlHistory = urlHistory;
        this.iconHistory = iconHistory;
        this.arrayQRCode = arrayQRCode;
    }

    public String getTitleHistory() {
        return titleHistory;
    }

    public String getUrlHistory() {
        return urlHistory;
    }

    public int getIconHistory() {
        return iconHistory;
    }

    public byte[] getArrayQRCode() {
        return arrayQRCode;
    }

    public boolean equals(Object obj)
    {

        if (!(obj instanceof HistoryItem)
        ) {
            return false;
        }
        else{
            HistoryItem historyItem = (HistoryItem) obj;
            return this.urlHistory.equals(historyItem.urlHistory) &&
                    this.titleHistory.equals(historyItem.titleHistory) &&
                    this.iconHistory == (historyItem.iconHistory);
        }
    }

}
