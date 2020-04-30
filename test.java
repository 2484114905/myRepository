import java.io.*;
import java.util.*;

public class test {
    public static void main(String[] args) throws NoSuchFieldException, CloneNotSupportedException, IOException, ClassNotFoundException {
        Scanner input = new Scanner(System.in);
        int times = input.nextInt();
        ArrayList<ArrayList<Fruit>> fruitList = new ArrayList<>();
        int[] peopleList = new int[times];
        for (int j = 0; j < times; j++){
            peopleList[j] = 0;
        }
        for (int i = 0; i < times; i++){
            int n = input.nextInt();
            int m = input.nextInt();
            peopleList[i] = m;

            ArrayList<Fruit> fruits = new ArrayList<>();
            for (int j = 0; j < n; j++){
                int number = input.nextInt();
                fruits.add(new Fruit(number,j));//通过输入顺序标识水果种类
            }
            fruitList.add(fruits);
        }
        for (int q=0; q < fruitList.size(); q++){
            distribute(fruitList.get(q), peopleList[q]);
        }

        input.close();
    }

    public static void distribute(ArrayList<Fruit> fruits, int people) throws NoSuchFieldException, CloneNotSupportedException, IOException, ClassNotFoundException {
         if(fruits.size() <= people){
             distribute1(fruits, people);
         }
         else{
             while (fruits.size() > people){
                 Collections.sort(fruits, new FruitComparator());
                 fruits.remove(0);
             }
             distribute2(fruits, people);
         }
    }

    public static void distribute1(ArrayList<Fruit> fruits, int people) throws CloneNotSupportedException, IOException, ClassNotFoundException {
        int kind = fruits.size();
        ArrayList<Fruit> tempFruits1 = clone(fruits);
        Collections.sort(tempFruits1, new FruitComparator());
        for (Fruit i:tempFruits1) {
            System.out.println(i.getNumber() + "id-"+i.getId()+"-");
        }

        int id = -1;
        while (kind < people){
            id = tempFruits1.get(tempFruits1.size()-1).getId();
            System.out.println("id" + id);
            ArrayList<Fruit> splitedFruit = fruits.get(id).split();
//            System.out.println("number" + fruits.get(tempFruits1.get(tempFruits1.size()-1).getId()).getNumber());
            for (Fruit i:splitedFruit) {
                System.out.println("-++-"+i.getNumber());
            }
            for (Fruit i:tempFruits1) {
                System.out.println("temp"+i.getNumber() + "--" +i.getId());
            }
            removeFruits(tempFruits1, id);
            for (Fruit i:tempFruits1) {
                System.out.println("afterremove"+i.getNumber() + "--" +i.getId());
            }
            tempFruits1.addAll(splitedFruit);

            Collections.sort(tempFruits1, new FruitComparator());
            if(tempFruits1.size() == people &&
                    fruits.get(id).getNumber()/ (fruits.get(id).getSplitCount()+1) > tempFruits1.get(0).getNumber()
            && splitedFruit.get(0).getNumber() == tempFruits1.get(tempFruits1.size()-1).getNumber()){
                tempFruits1.remove(0);
                removeFruits(tempFruits1, id);
                ArrayList<Fruit> nextsplitedFruit = fruits.get(id).split();
                tempFruits1.addAll(nextsplitedFruit);
                Collections.sort(tempFruits1, new FruitComparator());
            }
            else {
                for (Fruit i:tempFruits1) {
                    System.out.println("--"+i.getNumber());
                }
//              System.out.println("ok" + tempFruits1.get(2).getNumber());
            }
            kind = tempFruits1.size();
        }

        if (tempFruits1.get(tempFruits1.size()-1).getNumber() >= (tempFruits1.get(0).getNumber()*2 + 2)){
            distribute3(tempFruits1, fruits, people);
        }
        else {
            System.out.println(tempFruits1.get(0).getNumber());
        }
    }

    public static void distribute2(ArrayList<Fruit> fruits, int people) throws IOException, ClassNotFoundException {
        ArrayList<Fruit> tempFruits1 = clone(fruits);
        Collections.sort(tempFruits1, new FruitComparator());
        while (tempFruits1.get(tempFruits1.size()-1).getNumber() >= (tempFruits1.get(0).getNumber()*2 + 2)){
            int id = tempFruits1.get(tempFruits1.size()-1).getId();
            ArrayList<Fruit> splitedFruit = fruits.get(id).split();
            tempFruits1.remove(0);
            removeFruits(tempFruits1, id);//去除以前拆分的id类水果，重新拆分再加入列表
            tempFruits1.addAll(splitedFruit);
            Collections.sort(tempFruits1, new FruitComparator());
        }

        System.out.println(tempFruits1.get(0).getNumber());
    }
//distribute3相比于distribute增加了一个参数维持对初始水果列表的引用
    public static void distribute3(ArrayList<Fruit> currentFruits, ArrayList<Fruit> primaryFruits, int people){
        while (currentFruits.get(currentFruits.size()-1).getNumber() >= (currentFruits.get(0).getNumber()*2 + 2)){
            int id = currentFruits.get(currentFruits.size()-1).getId();//获取水果种类再进行拆分
            ArrayList<Fruit> splitedFruit = primaryFruits.get(id).split();
            currentFruits.remove(0);//去除min
            removeFruits(currentFruits, id);//去除以前拆分的id类水果，重新拆分再加入列表
            currentFruits.addAll(splitedFruit);
            Collections.sort(currentFruits, new FruitComparator());
        }

        System.out.println(currentFruits.get(0).getNumber());
    }

    public static class Fruit implements Serializable {
        private int id; //标识水果种类
        private int splitCount;//记录该种类水果拆分了几次
        private int number;//水果数量
        
        public Fruit(int number, int id){
            this.setNumber(number);
            this.setId(id);
            this.setSplitCount(1);
        }
        
        public ArrayList<Fruit> split(){ //将一种水果拆分，拆分以splitCount为依据，并以该种水果的总数进行拆分
            this.splitCount++;//如果一种水果上次被拆分为3份，这次应该被拆分为4份
            ArrayList<Fruit> splitedFruitList = new ArrayList<>();
            int remainder = this.number % this.splitCount;
            int quotient = this.number / this.splitCount;
//            System.out.println("quotient" + quotient);
            for (int i = 0; i < this.splitCount; i++){
                Fruit splitedFruit = new Fruit(quotient, this.id);
                splitedFruitList.add(splitedFruit);
            }
            for (int j = 0; j < remainder; j++){
                splitedFruitList.get(j).setNumber((splitedFruitList.get(j).getNumber()+1));
            }
            
            return splitedFruitList;
        }

        public Fruit deepClone()throws IOException,ClassNotFoundException {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bao);
            oos.writeObject(this);

            ByteArrayInputStream bai = new ByteArrayInputStream(bao.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bai);
            return (Fruit) ois.readObject();
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSplitCount() {
            return splitCount;
        }

        public void setSplitCount(int splitCount) {
            this.splitCount = splitCount;
        }
    }

    public static class FruitComparator implements Comparator<Fruit>{
        @Override
        public int compare(Fruit o1, Fruit o2) {
            if (o1.getNumber() < o2.getNumber()){
                return -1;
            }
            else if (o1.getNumber() > o2.getNumber()){
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    public static ArrayList<Fruit> clone(ArrayList<Fruit> fruits) throws IOException, ClassNotFoundException {
        ArrayList<Fruit> clonedFruit = new ArrayList<>();
        for (int i = 0; i < fruits.size(); i++){
            clonedFruit.add(fruits.get(i).deepClone());
        }
        return clonedFruit;
    }

    public static void removeFruits(ArrayList<Fruit> fruits, int id){

        int i = 0;
        while (i < fruits.size()){
            if (fruits.get(i).getId() == id){
                fruits.remove(i);
            }
            else {
                i++;
            }
        }
    }

}
