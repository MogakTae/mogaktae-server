package code.mogaktae.domain.common.client;

import code.mogaktae.global.exception.entity.RestApiException;
import code.mogaktae.global.exception.error.CustomErrorCode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class BaekjoonClient {

    public Boolean verifySolvedProblem(String solvedAcId, String problemId){
        return getTodaySolvedProblems(solvedAcId).contains(problemId);
    }

    private List<String> getTodaySolvedProblems(String solvedAcId){

        String url = "https://www.acmicpc.net/status?user_id=" + solvedAcId + "&result_id=4";

        List<String> todaySolvedProblems = new ArrayList<>();

        try{
            Document document = Jsoup.connect(url).get();

            LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Elements rows = document.select("tbody tr");

            for (Element row : rows) {

                Element problemIdLink = row.select("td:nth-child(3) a").first();
                Element solvedDateTimeLink = row.select("td:nth-child(9) a[title]").first();

                if(problemIdLink == null || solvedDateTimeLink == null){
                    continue;
                }

                String problemId = problemIdLink.text().trim();
                String solvedDatetime = solvedDateTimeLink.attr("title");

                if(solvedDatetime.length() >= 10){
                    LocalDate solvedDate = LocalDate.parse(solvedDatetime.substring(0,10), formatter);

                    if(solvedDate.equals(today)){
                        todaySolvedProblems.add(problemId);
                    }
                }
            }
        }catch(IOException e){
            throw new RestApiException(CustomErrorCode.PARSE_CRAWLING_DATA_FAILED);
        }
        System.out.println(todaySolvedProblems);
        return todaySolvedProblems;
    }
}
