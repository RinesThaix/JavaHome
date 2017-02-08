package ru.ifmo.ctddev.shandurenko;

import ru.ifmo.ctddev.shandurenko.walk.RecursiveWalk;

/**
 * Created by RINES on 08.02.17.
 */
public class MyTester {

    public static void main(String[] args) throws Exception {
//        Tester.main(new String[]{"RecursiveWalk", "ru.ifmo.ctddev.shandurenko.walk.RecursiveWalk"});
        RecursiveWalk.main(new String[]{"__Test__Walk__/test13_veryLargeFile.in", "__Test__Walk__/test13_veryLargeFile.out"});
    }

}
