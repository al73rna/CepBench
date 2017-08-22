package cepBench;

import java.util.ArrayList;

/**
 * Created by mohammadreza on 8/10/2016.
 */
public class FileComparer {

    private ArrayList<JEvent> onlyinAj;
    private ArrayList<JEvent> onlyinBj;
    private ArrayList<Pevent.event> onlyinAp;
    private ArrayList<Pevent.event> onlyinBp;

    public ArrayList<Pevent.event> getOnlyinBp() {
        return onlyinBp;
    }

    public ArrayList<Pevent.event> getOnlyinAp() {

        return onlyinAp;
    }

    public ArrayList<JEvent> getOnlyinAj() {

        return onlyinAj;
    }

    public ArrayList<JEvent> getOnlyinBj() {
        return onlyinBj;
    }


    public void compareJ(ArrayList<JEvent> A, ArrayList<JEvent>B) {

            onlyinAj = onlyAj(A,B);
            onlyinBj = onlyBj(A,B);

    }
    public void compareP(ArrayList<Pevent.event> A,ArrayList<Pevent.event>B) {

        onlyinAp = onlyAp(A,B);
        onlyinBp = onlyBp(A,B);

    }



    private ArrayList<JEvent> onlyAj(ArrayList<JEvent> A, ArrayList<JEvent>B){
        ArrayList<JEvent> onlyA = new ArrayList<>();
        for(JEvent a : A){
            boolean inB = false;
            for (JEvent b : B){
                if (isEqualJ(a,b)){
                    inB = true;
                    break;
                }
            }
            if (!inB){
                onlyA.add(a);
            }
        }
        return onlyA;
    }

    private ArrayList<JEvent> onlyBj(ArrayList<JEvent> A, ArrayList<JEvent>B) {
        ArrayList<JEvent> onlyB = new ArrayList<>();

        for (JEvent b : B) {
            boolean inA = false;
            for (JEvent a : A) {
                if (isEqualJ(a, b)) {
                    inA = true;
                    break;
                }
            }
            if (!inA) {
                onlyB.add(b);
            }

        }
        return onlyB;
    }

    private ArrayList<Pevent.event> onlyAp(ArrayList<Pevent.event> A,ArrayList<Pevent.event>B){
        ArrayList<Pevent.event> onlyA = new ArrayList<>();
        for (Pevent.event a:A){
            boolean inB = false;
            for (Pevent.event b :B){
                if(isEqualP(a,b)){
                    inB=true;
                    break;
                }
            }
            if(!inB){
                onlyA.add(a);
            }
        }

        return onlyA;
    }

    private ArrayList<Pevent.event> onlyBp(ArrayList<Pevent.event> A,ArrayList<Pevent.event>B){
        ArrayList<Pevent.event> onlyB = new ArrayList<>();
        for (Pevent.event b:B){
            boolean inA = false;
            for (Pevent.event a :A){
                if(isEqualP(a,b)){
                    inA=true;
                    break;
                }
            }
            if(!inA){
                onlyB.add(b);
            }
        }

        return onlyB;
    }


    public boolean isEqualJ(JEvent e1, JEvent e2) {
        if (e1.name.equals(e2.name)) {
            for (Item itm1 : e1.items) {
                boolean itemFound = false;
                for (Item itm2 : e2.items) {
                    if ((itm1.key.equals(itm2.key)) && (itm1.value.equals(itm2.value))) {
                        itemFound = true;
                        break;
                    }

                }
                if (!itemFound) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }


    private boolean isEqualP(Pevent.event e1, Pevent.event e2) {
        if (e1.getName().equals(e2.getName())) {
            for (Pevent.item itm1 : e1.getItemsList()) {
                boolean itemFound = false;
                for (Pevent.item itm2 : e2.getItemsList()) {
                    if ((itm1.getKey().equals(itm2.getKey())) && itm1.getValue().equals(itm2.getValue())) {
                        itemFound = true;
                        break;
                    }
                }
                if (!itemFound) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
