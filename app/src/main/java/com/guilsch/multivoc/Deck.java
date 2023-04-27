package com.guilsch.multivoc;

import android.os.Build;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.function.Predicate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class is the deck object which is an array list containing cards. It is only supposed to be
 * for the Global deck.
 *
 * @author Guilhem Schena
 */
public class Deck extends ArrayList<Card> {

    Deck() {
        super(1);
    }

    /**
     * Delete the card with the given uuid in the datafile
     *
     * @param cardUuid
     */
    public void deleteCardFromDatafile(String cardUuid) {
        try {
            FileInputStream inputFile = new FileInputStream(new File(Param.DATA_PATH));
            Workbook workbook = WorkbookFactory.create(inputFile);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Row header = rowIterator.next();

            int uuidIndex = utils.getFieldIndex(header, Param.UUID_FIELD_NAME);

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                Cell uuidCell = row.getCell(uuidIndex);

                if (uuidCell.getStringCellValue().compareTo(cardUuid) == 0){
                    // Delete row without shifting any row
                    int rowNb = row.getRowNum();
                    sheet.removeRow(row);
                    sheet.shiftRows(rowNb + 1, sheet.getLastRowNum(), -1);
                    break;
                }
            }

            inputFile.close();
            FileOutputStream outputStream = new FileOutputStream(Param.DATA_PATH);
            workbook.write(outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the card with the uuid in arg from the deck
     * @param uuid
     */
    public void deleteCardFromDeck(String uuid) {
        Card cardToDelete;
        Iterator<Card> iterator = this.iterator();
        while (iterator.hasNext()) {
            cardToDelete = iterator.next();

            if (cardToDelete.getUuid().compareTo(uuid) == 0) {
                iterator.remove();
            }
        }
    }

    public void init() {

        utils.cleanDataFile();
        // Adapted from
        // https://howtodoinjava.com/java/library/readingwriting-excel-files-in-java-poi-tutorial/
        try {
            // Create date formater
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

            FileInputStream file = new FileInputStream(new File(Param.DATA_PATH));

            // Create Workbook instance holding reference to excel file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            // Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Row header = rowIterator.next();

            int item1Index = utils.getFieldIndex(header, Param.ITEM1_FIELD_NAME);
            int item2Index = utils.getFieldIndex(header, Param.ITEM2_FIELD_NAME);
            int stateIndex = utils.getFieldIndex(header, Param.STATE_FIELD_NAME);
            int packIndex = utils.getFieldIndex(header, Param.PACK_FIELD_NAME);
            int nextPracticeDateIndex = utils.getFieldIndex(header, Param.NEXT_DATE_FIELD_NAME);
            int repetitionsIndex = utils.getFieldIndex(header, Param.REPETITIONS_FIELD_NAME);
            int easinessFactorIndex = utils.getFieldIndex(header, Param.EF_FIELD_NAME);
            int intervalIndex = utils.getFieldIndex(header, Param.INTERVAL_FIELD_NAME);
            int uuidIndex = utils.getFieldIndex(header, Param.UUID_FIELD_NAME);

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Cell currentCell = row.getCell(0);

                if (currentCell != null && row.getCell(stateIndex).getNumericCellValue() != Param.INVALID) {

                    String item1 = row.getCell(item1Index).getStringCellValue();
                    String item2 = row.getCell(item2Index).getStringCellValue();
                    int state = (int) row.getCell(stateIndex).getNumericCellValue();
                    String pack = row.getCell(packIndex).getStringCellValue();
                    Date nextPracticeDate = formatter.parse(row.getCell(nextPracticeDateIndex).getStringCellValue());
                    int repetitions = (int) row.getCell(repetitionsIndex).getNumericCellValue();
                    float easinessFactor = (float) row.getCell(easinessFactorIndex).getNumericCellValue();
                    int interval = (int) row.getCell(intervalIndex).getNumericCellValue();
                    String uuid = row.getCell(uuidIndex).getStringCellValue();
                    int rowNumber = row.getRowNum();

                    this.add(new Card(item1, item2, state, pack, nextPracticeDate, repetitions, easinessFactor, interval, uuid, rowNumber));

                }
            }

            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCards() {

        System.out.println("Cards in deck :");
        Iterator<Card> cardIterator = this.iterator();

        while(cardIterator.hasNext()){
            Card card = cardIterator.next();
            System.out.println("Item 1 : " + card.getItem1() + 
            "  |   Item 2 : " + card.getItem2() + 
            "  |   State : " + card.getState() +
            "  |   Pack : " + card.getPack() + 
            "  |   Next practice date : " + card.getNextPracticeDate() + 
            "  |   Repetitions : " + card.getRepetitions() + 
            "  |   Easiness factor : " + card.getEasinessFactor() + 
            "  |   Interval : " + card.getInterval() +
            "  |   UUID : " + card.getUuid());
        }
    }

    public void keepNFirst(int N) {
        if (this.size() > N) {
            this.subList(0, N);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filter() {

        Iterator<Card> iterator = this.iterator();
        Predicate<Card> pred = x -> x.getInterval() > 3;

        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (!pred.test(card)) {
                iterator.remove();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterToTrain() {

        Iterator<Card> iterator = this.iterator();
        Predicate<Card> pred = x -> x.getNextPracticeDate().compareTo(utils.giveCurrentDate()) < 0 && x.getState() == Param.ACTIVE ;

        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (!pred.test(card)) {
                iterator.remove();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterToLearn() {

        Iterator<Card> iterator = this.iterator();
        Predicate<Card> pred = x -> x.getState() == Param.TO_LEARN;

        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (!pred.test(card)) {
                iterator.remove();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterActive() {

        Iterator<Card> iterator = this.iterator();
        Predicate<Card> pred = x -> x.getState() == Param.ACTIVE;

        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (!pred.test(card)) {
                iterator.remove();
            }
        }

    }

    public int getCardsWithStateSNb(int S) {
        int count = 0;
        for (Card card : this) {
            if (card.getState() == S) {
                count++;
            }
        }
        return count;
    }

    public int getCardsToReviewNb() {
        int count = 0;
        for (Card card : this) {
            if (card.getNextPracticeDate().before(utils.giveCurrentDate()) && card.getState() == 1) {
                count++;
            }
        }
        return count;
    }

    public int getCardsToLearnNb() {
        int count = 0;
        for (Card card : this) {
            if (card.getState() == 2) {
                count++;
            }
        }
        return count;
    }

    public void updateDeckDataVariables() {
        // TODO modify to create and change attributes of the deck
        this.updateCardsStatesVariables();
        Param.CARDS_NB = this.size();
        Param.ACTIVE_CARDS_NB = this.getCardsWithStateSNb(1);
    }

    /**
     * Update Param.CARDS_TO_REVIEW_NB and Param.CARDS_TO_LEARN_NB variables
     */
    public void updateCardsStatesVariables() {
        int countToTrain = 0;
        int countToLearn = 0;
        for (Card card : this) {
            if (card.getNextPracticeDate().before(utils.giveCurrentDate()) && card.getState() == 1) {
                countToTrain++;
            }
            if (card.getState()==2){
                countToLearn++;
            }
        }

        Param.CARDS_TO_REVIEW_NB = countToTrain;
        Param.CARDS_TO_LEARN_NB = countToLearn;

        return;
    }

    /**
     * Returns a queue containing all cards to be trained in random order
     */
    public Queue<Card> getTrainingQueue() {

        Queue<Card> revision_queue = new LinkedList<>();
        Predicate<Card> pred = x -> x.getNextPracticeDate().compareTo(utils.giveCurrentDate())
                < 0 && x.getState() == Param.ACTIVE;

        Iterator<Card> iterator = this.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (pred.test(card)) {
                revision_queue.add(card);
            }
        }

        // Shuffle the queue
        List<Card> list_to_shuffle = new LinkedList<>(revision_queue);
        Collections.shuffle(list_to_shuffle);
        revision_queue = new LinkedList<>(list_to_shuffle);

        return revision_queue;
    }

//    public void updateCardInDeck(String uuid) {
//        Iterator<Card> iterator = this.iterator();
//    }

//    /**
//     * Iterates over all deck cards and for the cards with a uuid that is one of the uuid of a card
//     * in the queue, modifies the card in the deck from the card in the queue and save it in the datafile
//     */
//    public void updateDeckAndDatabaseFromQueue(Queue<Card> cardsQueue, ProgressBar progressBar) {
//
//        Map<String, Card> queueCardMap = new HashMap<>();
//        for (Card card : cardsQueue) {
//            queueCardMap.put(card.getUuid(), card);
//        }
//
//        int progress = 0;
//        // Iterate through cards of the deck
//        for (Card cardInDeck : this) {
//            String uuid = cardInDeck.getUuid();
//            if (queueCardMap.containsKey(uuid)) {
//                // Find card with corresponding uuid
//                Card cardInQueue = queueCardMap.get(uuid);
//
//                // Update parameters in the deck and in the database
//                cardInDeck.updateParameters(cardInQueue.getNextPracticeDate(),
//                        cardInQueue.getRepetitions(), cardInQueue.getEasinessFactor(),
//                        cardInQueue.getInterval());
//                cardInDeck.updateInDatabase();
//            }
//            progress += 1;
//            progressBar.setProgress(progress);
//        }
//    }

    /**
     * Returns a list containing all the cards with state to learn (cards that will be selected or
     * not by the user in the Learn activity) of the deck
     *
     * @return List<Card> with cards to learn
     */
    public List<Card> getCardsToLearnList() {
        List<Card> toLearnList = new ArrayList<Card>();

        Iterator<Card> iterator = this.iterator();
        Predicate<Card> pred = x -> x.getState() == Param.TO_LEARN;

        while (iterator.hasNext()) {
            Card card = iterator.next();

            if (pred.test(card)) {
                toLearnList.add(card);
            }
        }

        return toLearnList;
    }

    /**
     * Returns the card in the deck with the given uuid
     * @param uuid
     * @return Card with the uuid
     */
    public Card getCardFromUuid(String uuid) {
        Card card = null;
        Iterator<Card> iterator = this.iterator();

        while (iterator.hasNext()) {
            card = iterator.next();

            if (card.getUuid().compareTo(uuid) == 0) {
                return card;
            }
        }

        return card;
    }
}