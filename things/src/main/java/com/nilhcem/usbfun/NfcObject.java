package com.nilhcem.usbfun;

public enum NfcObject {
    UNKNOWN(R.drawable.empty, null),
    CARD(R.drawable.card, "<TODO: TAG_ID>"),
    KEYRING(R.drawable.keyring, "<TODO: TAG_ID>");

    public final int imageRes;
    public final String nfcTag;

    NfcObject(int imageRes, String nfcTag) {
        this.imageRes = imageRes;
        this.nfcTag = nfcTag;
    }

    public static NfcObject findByTag(String tag) {
        for (NfcObject obj : NfcObject.values()) {
            if (tag.equalsIgnoreCase(obj.nfcTag)) {
                return obj;
            }
        }
        return UNKNOWN;
    }
}
