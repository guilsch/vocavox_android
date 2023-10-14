package com.guilsch.multivoc;

import java.util.Comparator;
import java.util.Date;
import java.util.function.BiPredicate;

public class ComparatorCard implements Comparator<Card> {
    private int sortingCriterion;
    private boolean sortingOrder;

    public ComparatorCard(int sortingCriterion) {
        this.sortingCriterion = sortingCriterion;
    }

    @Override
    public int compare(Card card1, Card card2) {
        if (sortingCriterion == Param.SORT_BY_CREATION_DATE) {

            Date date1 = card1.getCreationDate();
            Date date2 = card2.getCreationDate();

            if (date1.before(date2)) {
                return manageOrder(1);
            } else if (date1.after(date2)) {
                return manageOrder(-1);
            } else {
                return 0;
            }
        }

        if (sortingCriterion == Param.SORT_BY_TRAINING_DATE) {

            Date date1 = card1.getNextPracticeDate();
            Date date2 = card2.getNextPracticeDate();

            if (date1.before(date2)) {
                return manageOrder(1);
            } else if (date1.after(date2)) {
                return manageOrder(-1);
            } else {
                return 0;
            }
        }

        else if (sortingCriterion == Param.SORT_BY_TRAINING_DATE) {
            return manageOrder(card1.getCreationDate().compareTo(card2.getCreationDate()));
        }

        else if (sortingCriterion == Param.SORT_ALPHABETICALLY_USER) {
            String name1 = card1.getItem1();
            String name2 = card2.getItem1();
            return manageOrder(getSign(name1.compareTo(name2)));
        }

//        else if (sortingCriterion == Param.SORT_ALPHABETICALLY_TARGET) {
//            String name1 = card1.getItem2();
//            String name2 = card2.getItem2();
//            return getSign(name1.compareTo(name2));
//        }

        else {
                throw new IllegalArgumentException("Unvalid sorting criterion");
        }
    }

    public void setSortingLogic(int criterion) {
        switch (criterion) {
            case Param.SORT_BY_CREATION_DATE:
                this.sortingCriterion = criterion;
                break;
            case Param.SORT_BY_TRAINING_DATE:
                this.sortingCriterion = criterion;
                break;
            case Param.SORT_ALPHABETICALLY_USER:
                this.sortingCriterion = criterion;
                break;
            default:
                throw new IllegalArgumentException("Unvalid sorting criterion");
        }
    }

    private int getSign(int number) {
        if (number < 0){return -1;}
        else if (number > 0){return 1;}
        else if (number == 0){return 0;}
        else {throw new IllegalArgumentException("Maths don't work");}
    }

    public void setSortingOrder(Boolean sortOrder) {
        this.sortingOrder = sortOrder;
    }

    private int manageOrder(int sortAnswer) {
        if (!sortingOrder) {
            switch (sortAnswer) {
                case -1:
                    return 1;
                case 1:
                    return -1;
                case 0:
                    return 0;
                default:
                    throw new IllegalArgumentException("Sort answer must be -1, 0 or 1");
            }
        }
        return sortAnswer;
    }
}

