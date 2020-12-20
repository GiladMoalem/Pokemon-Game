package tests;

public class bdikaString {
    public static void main(String[] a){
        String s="32.127,872.298,78.00";
        int first = 0,second = 0;
        for (int ch=0; ch < s.length(); ch++){
            if(s.charAt(ch)==',')
                if(first==0)
                    first=ch;
                else second=ch;
        }
        String st1 = s.substring(0,first);
        String st2 = s.substring(first+1,second);
        String st3 = s.substring(second+1);
        System.out.println(st1+"   "+st2+"    "+st3);

        double d1 = Double.parseDouble(st1);
        System.out.println(d1);
    }
}
