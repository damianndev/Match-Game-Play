package com.example.MatchGamePlay.Utils;

import java.util.Random;

public class Utils {

        public static int losuj(int minimum, int maximum) {
            Random rn = new Random();
            return rn.nextInt(maximum - minimum + 1) + minimum;
        }
}
