package net.diskroom.weather;


public class weatherCast{
    private String date="";     //日期
    private String weekday="";       //星期
    private String dir="";       //风向
    private String pop="";       //降水概率


    public void setDate(String date){
        this.date = date;
    }

    public void setWeekDay(String weekday){
        this.weekday = weekday;
    }

    public void setDir(String dir){ this.dir = dir; }

    public void setPop(String pop){
        this.pop = pop;
    }

    public String getDate(){ return this.date.substring(0,this.date.length()-4);}

    public String getDateInfo(){ return this.date; }

    public String getYear(){ return this.date.substring(this.date.length()-4); }

    public String getWeekday(){ return this.weekday; }

    public String getWeekdayInfo(){
        switch (this.weekday){
            case "Mon":
                return "Monday";
            case "Tue":
                return "Tuesday";
            case "Wed":
                return "Wednesday";
            case "Thu":
                return "Thursday";
            case "Fri":
                return "Friday";
            case "Sat":
                return "Saturday";
            case "Sun":
                return "Sunday";
            default:
                return "";
        }
    }

    public String getDir(){
        return this.dir;
    }

    public String getPop(){
        return this.pop;
    }
}