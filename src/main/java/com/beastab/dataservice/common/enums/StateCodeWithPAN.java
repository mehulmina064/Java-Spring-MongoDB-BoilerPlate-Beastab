package com.beastab.dataservice.common.enums;

public class StateCodeWithPAN {
    private static final String[] stateCodes = {
            "35", // AN - ANDAMAN AND NICOBAR
            "28", // AP - ANDHRA PRADESH
            "37", // AD - ANDHRA PRADESH(NEW)
            "12", // AR - ARUNACHAL PRADESH
            "18", // AS - ASSAM
            "10", // BH - BIHAR
            "04", // CH - CHANDIGARH
            "22", // CT - CHATTISGARH
            "26", // DN - DADRA AND NAGAR HAVELI
            "25", // DD - DAMAN AND DIU
            "07", // DL - DELHI
            "30", // GA - GOA
            "24", // GJ - GUJARAT
            "06", // HR - HARYANA
            "02", // HP - HIMACHAL PRADESH
            "01", // JK - JAMMU AND KASHMIR
            "20", // JH - JHARKHAND
            "29", // KA - KARNATAKA
            "32", // KL - KERALA
            "31", // LD - LAKSHADWEEP ISLAND
            "23", // MP - MADHYA PRADESH
            "27", // MH - MAHARASHTRA
            "14", // MN - MANIPUR
            "17", // ME - MEGHALAYA
            "15", // MI - MIZORAM
            "13", // NL - NAGALAND
            "21", // OR - ODISHA
            "34", // PY - PONDICHERRY
            "03", // PB - PUNJAB
            "08", // RJ - RAJASTHAN
            "11", // SK - SIKKIM
            "33", // TN - TAMIL NADU
            "36", // TS - TELANGANA
            "16", // TR - TRIPURA
            "09", // UP - UTTAR PRADESH
            "05", // UT - UTTARAKHAND
            "19"  // WB - WEST BENGAL
    };

    private static final String[] stateChars = {
            "AN", // ANDAMAN AND NICOBAR
            "AP", // ANDHRA PRADESH
            "AD", // ANDHRA PRADESH(NEW)
            "AR", // ARUNACHAL PRADESH
            "AS", // ASSAM
            "BH", // BIHAR
            "CH", // CHANDIGARH
            "CT", // CHATTISGARH
            "DN", // DADRA AND NAGAR HAVELI
            "DD", // DAMAN AND DIU
            "DL", // DELHI
            "GA", // GOA
            "GJ", // GUJARAT
            "HR", // HARYANA
            "HP", // HIMACHAL PRADESH
            "JK", // JAMMU AND KASHMIR
            "JH", // JHARKHAND
            "KA", // KARNATAKA
            "KL", // KERALA
            "LD", // LAKSHADWEEP ISLAND
            "MP", // MADHYA PRADESH
            "MH", // MAHARASHTRA
            "MN", // MANIPUR
            "ME", // MEGHALAYA
            "MI", // MIZORAM
            "NL", // NAGALAND
            "OR", // ODISHA
            "PY", // PONDICHERRY
            "PB", // PUNJAB
            "RJ", // RAJASTHAN
            "SK", // SIKKIM
            "TN", // TAMIL NADU
            "TS", // TELANGANA
            "TR", // TRIPURA
            "UP", // UTTAR PRADESH
            "UT", // UTTARAKHAND
            "WB"  // WEST BENGAL
    };

    public static String[] getStateCodes() {
        return stateCodes;
    }

    public static String[] getStateChars() {
        return stateChars;
    }
}
