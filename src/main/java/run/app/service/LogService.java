package run.app.service;

import org.springframework.data.domain.Page;
import run.app.model.dto.LogDTO;
import run.app.model.entity.Log;
import run.app.service.base.CrudService;

/**
 * Log service interface.
 *
 * @author johnniang
 * @date 2019-03-14
 */
public interface LogService extends CrudService<Log, Long> {

    /**
     * Lists latest logs.
     *
     * @param top top number must not be less than 0
     * @return a page of latest logs
     */
    Page<LogDTO> pageLatest(int top);
}
