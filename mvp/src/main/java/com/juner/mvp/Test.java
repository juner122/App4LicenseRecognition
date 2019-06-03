package com.juner.mvp;

import java.util.HashMap;

public class Test {

    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int com = target - nums[i];
            if (hashMap.containsKey(com)) {

                return new int[]{hashMap.get(com), i};

            }
            hashMap.put(nums[i], i);

        }
        throw new IllegalArgumentException("No two sum solution");
    }
}
