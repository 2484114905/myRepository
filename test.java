import java.util.*;

public class test {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int times = input.nextInt();
        ArrayList<ArrayList<Integer>> fruitList = new ArrayList<>();
        int[] peopleList = new int[times];
        for (int j = 0; j < times; j++){
            peopleList[j] = 0;
        }
        for (int i = 0; i < times; i++){
            int n = input.nextInt();
            int m = input.nextInt();
            peopleList[i] = m;

            ArrayList<Integer> fruits = new ArrayList<>();
            for (int j = 0; j < n; j++){
                int number = input.nextInt();
                fruits.add(number);
            }
            fruitList.add(fruits);
        }
        for (int q=0; q < fruitList.size(); q++){
            distribute(fruitList.get(q), peopleList[q]);
        }
    }

    public static void distribute(ArrayList<Integer> fruits, int people){
         if(fruits.size() <= people){
             distribute1(fruits, people);
         }
         else{
             while (fruits.size() > people){
                 Collections.sort(fruits);
                 fruits.remove(0);
             }
             distribute2(fruits, people);
         }
    }

    public static void distribute1(ArrayList<Integer> fruits, int people){
        Collections.sort(fruits);
//        for (Integer i:fruits) {
//            System.out.print(i);
//        }
        while (fruits.size() < people){
            int newKinds = fruits.get(fruits.size()-1) - fruits.get(0);
            fruits.add(newKinds);
            fruits.set(fruits.size()-2, fruits.get(fruits.size()-2)-newKinds);
            Collections.sort(fruits);
        }

        if (fruits.get(fruits.size()-1) > (fruits.get(0)*2 + 2)){
            distribute2(fruits, people);
        }
        else {
            System.out.println(fruits.get(0));
        }
    }

    public static void distribute2(ArrayList<Integer> fruits, int people){
        while (fruits.get(fruits.size()-1) > (fruits.get(0)*2 + 2)){
            int newKinds1 = fruits.get(fruits.size()-1) / 2;
            int newKinds2 = fruits.get(fruits.size()-1) - newKinds1;
            fruits.set(fruits.size()-1, newKinds1);
            fruits.add(newKinds2);
            fruits.remove(0);
            Collections.sort(fruits);
        }

        System.out.println(fruits.get(0));
    }
}
