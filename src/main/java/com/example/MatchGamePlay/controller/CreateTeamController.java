package com.example.MatchGamePlay.controller;

import com.example.MatchGamePlay.Utils.Utils;
import com.example.MatchGamePlay.controller.dto.TeamRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController

public class CreateTeamController {

    @RequestMapping(method = RequestMethod.POST, path = "/new-team-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newTeam(@RequestBody TeamRequest teamRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/create-team";

        ResponseEntity<String> response = restTemplate.postForEntity(url, teamRequest, String.class);

        return response;
    }


    @RequestMapping(method = RequestMethod.POST, path = "/new-team2-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newTeam2(@RequestParam("typTrenera") String typTrenera, @RequestParam("nrUstawienia") Integer nrUstawienia) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/create-team-params?typTrenera={typTrenera}&nrUstawienia={nrUstawienia}";
        String correctUrl = url.replace("{typTrenera}", typTrenera).replace("{nrUstawienia}", nrUstawienia.toString());

        ResponseEntity<String> response = restTemplate.getForEntity(correctUrl, String.class);

        return response;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/new-team-bot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newTeamBot() {
        RestTemplate restTemplate = new RestTemplate();

        List<String> collectCoaches = getCollectionFromCreateTeamService(restTemplate,"http://localhost:8080/coaches");
        int indexCoach = Utils.losuj(0, collectCoaches.size() - 1);
        String coachType = collectCoaches.get(indexCoach);

        List<String> collectLineups = getCollectionFromCreateTeamService(restTemplate,"http://localhost:8080/lineups");
        int indexLineup = Utils.losuj(0, collectLineups.size() - 1);
        Integer lineupNumber = Integer.valueOf(collectLineups.get(indexLineup));

        String teamRequestUrl = "http://localhost:8080/create-team";
        TeamRequest teamRequest = new TeamRequest(coachType, lineupNumber);
        ResponseEntity<String> teamResponse = restTemplate.postForEntity(teamRequestUrl, teamRequest, String.class);

        return teamResponse;

    }

    private List<String> getCollectionFromCreateTeamService(RestTemplate restTemplate,String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String[] split = response.getBody().split(",");
        List<String> list = Arrays.asList(split);
        List<String> collect = list.stream()
                .map(e1 -> e1.replace("[", ""))
                .map(e2 -> e2.replace("]", ""))
                .map(e3 -> e3.replace("\"",""))
                .collect(Collectors.toList());

        return collect;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getLineup/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLineup(@PathVariable (value = "number") Integer number) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/lineup/{number}";
        String correctUrl = url.replace("{number}", number.toString());
        ResponseEntity<String> response = restTemplate.getForEntity(correctUrl, String.class);

        return response;

    }

    @RequestMapping(method = RequestMethod.GET, path = "/getLineups", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getLineups(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/lineups/";

        return getCollectionFromCreateTeamService(restTemplate, url);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/getCoaches", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getCoaches(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/coaches/";

        return getCollectionFromCreateTeamService(restTemplate, url);

    }
}




//TODO: 2. poćwiczyć kontrolery

//TODO: 3. wypchnąć na gita

//TODO: 4. zamienić String.class na Team.class i zmienić w getLineups też name a nie same numery

//TODO: 5. pomyśleć schemat aplikacji

//TODO: 6. przetłumaczyć

//TODO: 7. zmienić nazwę pierwszej aplikacji

//TODO: 8. error handling



//https://www.baeldung.com/spring-resttemplate-post-json