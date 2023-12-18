package com.guilsch.vocavox;

import java.text.Collator;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

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

            if (card1.getState() != Param.ACTIVE) {
                if (card2.getState() != Param.ACTIVE) {
                    // Both cards are not active
                    return compareComparableDates(date1, date2);
                }
                else {
                    // Only card 2 is active
                    return -1;
                }
            } else {
                if (card2.getState() != Param.ACTIVE) {
                    // Only card 1 is active
                    return 1;
                }
                else {
                    // Both card are active
                    return compareComparableDates(date1, date2);
                }
            }
        }

        else if (sortingCriterion == Param.SORT_BY_TRAINING_DATE) {
            return manageOrder(card1.getCreationDate().compareTo(card2.getCreationDate()));
        }

        else if (sortingCriterion == Param.SORT_ALPHABETICALLY_USER) {
            String name1 = card1.getItem1();
            String name2 = card2.getItem1();

            Collator collator = Collator.getInstance(Locale.ROOT);

            return manageOrder(getSign(collator.compare(name1.trim(), name2.trim())));
        }

        else {
                throw new IllegalArgumentException("Unvalid sorting criterion");
        }
    }

    private int compareComparableDates(Date date1, Date date2) {
        if (date1.before(date2)) {
            return manageOrder(1);
        } else if (date1.after(date2)) {
            return manageOrder(-1);
        } else {
            return 0;
        }
    }

    public void setSortingLogic(int criterion) {
        if(criterion == Param.SORT_ALPHABETICALLY_USER || criterion == Param.SORT_BY_CREATION_DATE
                || criterion == Param.SORT_BY_TRAINING_DATE) {
            this.sortingCriterion = criterion;
        } else {
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

