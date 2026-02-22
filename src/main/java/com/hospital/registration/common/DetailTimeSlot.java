package com.hospital.registration.common;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: DetailTimeSlot
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 细分时段配置
 */
@Getter
public class DetailTimeSlot {

    private final String slotCode;
    private final String timeRange;
    private final String period;
    private final Integer sortOrder;

    public DetailTimeSlot(String slotCode, String timeRange, String period, Integer sortOrder) {
        this.slotCode = slotCode;
        this.timeRange = timeRange;
        this.period = period;
        this.sortOrder = sortOrder;
    }

    /**
     * 获取上午的细分时段列表
     */
    public static List<DetailTimeSlot> getMorningSlots() {
        List<DetailTimeSlot> slots = new ArrayList<>();
        slots.add(new DetailTimeSlot("M_0800", "08:00-08:30", "MORNING", 1));
        slots.add(new DetailTimeSlot("M_0830", "08:30-09:00", "MORNING", 2));
        slots.add(new DetailTimeSlot("M_0900", "09:00-09:30", "MORNING", 3));
        slots.add(new DetailTimeSlot("M_0930", "09:30-10:00", "MORNING", 4));
        slots.add(new DetailTimeSlot("M_1000", "10:00-10:30", "MORNING", 5));
        slots.add(new DetailTimeSlot("M_1030", "10:30-11:00", "MORNING", 6));
        slots.add(new DetailTimeSlot("M_1100", "11:00-11:30", "MORNING", 7));
        slots.add(new DetailTimeSlot("M_1130", "11:30-12:00", "MORNING", 8));
        return slots;
    }

    /**
     * 获取下午的细分时段列表
     */
    public static List<DetailTimeSlot> getAfternoonSlots() {
        List<DetailTimeSlot> slots = new ArrayList<>();
        slots.add(new DetailTimeSlot("A_1400", "14:00-14:30", "AFTERNOON", 9));
        slots.add(new DetailTimeSlot("A_1430", "14:30-15:00", "AFTERNOON", 10));
        slots.add(new DetailTimeSlot("A_1500", "15:00-15:30", "AFTERNOON", 11));
        slots.add(new DetailTimeSlot("A_1530", "15:30-16:00", "AFTERNOON", 12));
        slots.add(new DetailTimeSlot("A_1600", "16:00-16:30", "AFTERNOON", 13));
        slots.add(new DetailTimeSlot("A_1630", "16:30-17:00", "AFTERNOON", 14));
        slots.add(new DetailTimeSlot("A_1700", "17:00-17:30", "AFTERNOON", 15));
        slots.add(new DetailTimeSlot("A_1730", "17:30-18:00", "AFTERNOON", 16));
        return slots;
    }

    /**
     * 获取晚间的细分时段列表
     */
    public static List<DetailTimeSlot> getEveningSlots() {
        List<DetailTimeSlot> slots = new ArrayList<>();
        slots.add(new DetailTimeSlot("E_1800", "18:00-18:30", "EVENING", 17));
        slots.add(new DetailTimeSlot("E_1830", "18:30-19:00", "EVENING", 18));
        slots.add(new DetailTimeSlot("E_1900", "19:00-19:30", "EVENING", 19));
        slots.add(new DetailTimeSlot("E_1930", "19:30-20:00", "EVENING", 20));
        return slots;
    }

    /**
     * 根据大时段获取细分时段列表
     */
    public static List<DetailTimeSlot> getSlotsByTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == TimeSlot.MORNING) {
            return getMorningSlots();
        } else if (timeSlot == TimeSlot.AFTERNOON) {
            return getAfternoonSlots();
        } else if (timeSlot == TimeSlot.EVENING) {
            return getEveningSlots();
        }
        return new ArrayList<>();
    }
}
