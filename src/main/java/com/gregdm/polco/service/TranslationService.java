package com.gregdm.polco.service;

import com.gregdm.polco.domain.BadWord;
import com.gregdm.polco.domain.GoodWord;
import com.gregdm.polco.repository.BadWordRepository;
import com.gregdm.polco.repository.GoodWordRepository;
import com.gregdm.polco.repository.SearchBadWordRepository;
import com.gregdm.polco.repository.SearchGoodWordRepository;
import liquibase.util.csv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
@Transactional
public class TranslationService {

    private final Logger log = LoggerFactory.getLogger(TranslationService.class);

    @Inject
    private BadWordRepository badWordRepository;

    @Inject
    private GoodWordRepository goodWordRepository;

    @Inject
    private SearchGoodWordRepository searchGoodWordRepository;

    @Inject
    private SearchBadWordRepository searchBadWordRepository;

    public String translateText(String text){


        return text + "Greg";
    }

    public String importCSV(MultipartFile file){
        if (!file.isEmpty()) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
            String[] row = null;
            while((row = reader.readNext()) != null) {
                if(row.length>1){

                    BadWord badWord = new BadWord();
                    List<BadWord> badWordFind = searchBadWordRepository.findByValue(row[0].trim().toLowerCase());
                    if(CollectionUtils.isEmpty(badWordFind)){
                        badWord.setValue(row[0].trim().toLowerCase());
                        badWord.setType("NONE");
                        badWord = badWordRepository.save(badWord);
                    } else {
                        badWord = badWordFind.iterator().next();
                    }

                    for(int i=1; i<row.length-1; i++) {
                        boolean goodWordPresent = false;


                        //Si la traduction est déjà présente
                        for(GoodWord good : badWord.getTranslations()){
                            if(good.getValue().equals(row[i])){
                                goodWordPresent = true;
                                break;
                            }
                        }

                        //Sinon on l'ajoute au BadWord
                        if(!goodWordPresent){
                            GoodWord goodWord = new GoodWord();
                            goodWord.setValue(row[i].trim().toLowerCase());
                            goodWord.setLevel("NORMAL");
                            goodWord.setBadWord(badWord);
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "fILE Greg";

    } else {
        return "You failed to upload " + file.getName() + " because the file was empty.";
    }
    }
}
