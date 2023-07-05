package searchengine.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import searchengine.config.SitesList;
import searchengine.dto.statistics.DetailedStatisticsItem;
import searchengine.dto.statistics.StatisticsData;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.services.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import searchengine.model.Site;
import searchengine.model.Status;
import searchengine.services.indexresponseentity.Detailed;
import searchengine.services.indexresponseentity.Statistics;
import searchengine.services.indexresponseentity.Total;
import searchengine.services.responses.StatisticResponseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private static final Log log = LogFactory.getLog(StatisticsServiceImpl.class);

    private final SiteRepositoryService siteRepositoryService;

    private final LemmaRepositoryService lemmaRepositoryService;

    private final PageRepositoryService pageRepositoryService;
    private final SitesList sites;


    @Override
    public StatisticResponseService getStatistics() {
        Total total = getTotal();
        total.setSites(sites.getSites().size());
        total.setIndexing(true);

        List<DetailedStatisticsItem> detailed = new ArrayList<>();
        List<Site> siteList = siteRepositoryService.getAllSites();
        Detailed[] detaileds = new Detailed[siteList.size()];
        for (int i = 0; i < siteList.size(); i++) {
            detaileds[i] = getDetailed(siteList.get(i));
            Site site = siteList.get(i);
            DetailedStatisticsItem item = new DetailedStatisticsItem();
            item.setName(site.getName());
            item.setUrl(site.getUrl());
            detailed.add(item);

        }

        log.info("Получение статистики.");
        return new StatisticResponseService(true, new Statistics(total, detaileds));
    }

    private Total getTotal() {
        long sites = siteRepositoryService.siteCount();
        long lemmas = lemmaRepositoryService.lemmaCount();
        long pages = pageRepositoryService.pageCount();
        boolean isIndexing = isSitesIndexing();
        return new Total(sites, pages, lemmas, isIndexing);

    }

    private Detailed getDetailed(Site site) {
        String url = site.getUrl();
        String name = site.getName();
        Status status = site.getStatus();
        long statusTime = site.getStatusTime().getTime();
        String error = site.getLastError();
        long pages = pageRepositoryService.pageCount(site.getId());
        long lemmas = lemmaRepositoryService.lemmaCount(site.getId());
        return new Detailed(url, name, status, statusTime, error, pages, lemmas);
    }

    private boolean isSitesIndexing() {
        boolean is = false;
        for (Site s : siteRepositoryService.getAllSites()) {
            if (s.getStatus().equals(Status.INDEXING)) {
                is = true;
                break;
            }
        }
        return is;
    }
}
