package de.htwdd.htwdresden.types.semesterplan;

/**
 * Class to store FreeDays
 * Created by Vitali Drazdovich , Artyom Dyadechkin
 */
public final class FreeDay extends Period {
    private String name;

    @Override
    public String toString() {
        return super.toString() + "\n";
    }

    public String getName() {
        return name + "\n";
    }

    public String getBeginDay() {
        return super.getBeginDay();
    }

    public String getEndDay() {
        return super.getEndDay();
    }

    public FreeDay(String name, String beginDay, String endDay) {
        super(beginDay, endDay);
        this.name = name;
    }
}