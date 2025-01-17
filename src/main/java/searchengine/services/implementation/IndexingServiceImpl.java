package searchengine.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import searchengine.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import searchengine.IndexBuilding;
import searchengine.services.responses.FalseResponseService;
import searchengine.services.responses.ResponseService;
import searchengine.services.responses.TrueResponseService;

@Service
public class IndexingServiceImpl implements IndexingService {

    private final IndexBuilding indexBuilding;

    private static final Logger log = LogManager.getLogger();

    public IndexingServiceImpl(IndexBuilding indexBuilding) {
        this.indexBuilding = indexBuilding;
    }

    @Override
    public ResponseService startIndexingAll() {
        ResponseService response;
        boolean indexing;
        try {
            indexing = indexBuilding.allSiteIndexing();
            log.info("Попытка запуска индексации всех сайтов" );
        } catch (InterruptedException e) {
            response = new FalseResponseService("Неизвестная ошибка запуска индексации");
            log.error("Ошибка при запуске индексации", e);
            return response;
        }
        if (indexing) {
            response = new TrueResponseService();
            log.info("Индексация сайтов запущена");
        } else {
            response = new FalseResponseService("Индексация уже запущена");
            log.warn("Индексация сайтов не запущена. Процесс индексации был запущен ранее.");
        }
        return response;
    }

    @Override
    public ResponseService stopIndexing() {
        boolean indexing = indexBuilding.stopSiteIndexing();
        log.info("Попытка остановки индексации");
        ResponseService response;
        if (indexing) {
            response = new TrueResponseService();
            log.info("Индексация сайта остановлена");
        } else {
            response = new FalseResponseService("Индексация не запущена или уже завершена");
            log.warn("Остановка индексации не может быть выполнена, процесс индексации не запущен.");
        }
        return response;
    }

    @Override
    public ResponseService startIndexingOne(String url) {
        ResponseService resp;
        String response;
        try {
            response = indexBuilding.checkedSiteIndexing(url);
        } catch (InterruptedException e) {
            resp = new FalseResponseService("Ошибка запуска индексации");
            return resp;
        }
        if (response.equals("not found")) {
            resp = new FalseResponseService("Страница находится за пределами сайтов," +
                    " указанных в конфигурационном файле");
        } else if (response.equals("false")) {
            resp = new FalseResponseService("Индексация страницы уже запущена " );
        } else {
            resp = new TrueResponseService();
        }
        return resp;
    }
}
