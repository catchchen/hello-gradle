package run.app.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import run.app.exception.NotFoundException;
import run.app.model.entity.JournalComment;
import run.app.repository.JournalCommentRepository;
import run.app.repository.JournalRepository;
import run.app.service.JournalCommentService;
import run.app.service.OptionService;
import run.app.service.UserService;
import run.app.service.assembler.comment.JournalCommentAssembler;

/**
 * Journal comment service implementation.
 *
 * @author johnniang
 * @author guqing
 * @date 2019-04-25
 */
@Service
public class JournalCommentServiceImpl extends BaseCommentServiceImpl<JournalComment>
    implements JournalCommentService {

    private final JournalRepository journalRepository;

    public JournalCommentServiceImpl(JournalCommentRepository journalCommentRepository,
        OptionService optionService,
        UserService userService,
        ApplicationEventPublisher eventPublisher,
        JournalRepository journalRepository,
        JournalCommentAssembler journalCommentAssembler) {
        super(journalCommentRepository, optionService, userService, eventPublisher,
            journalCommentAssembler);
        this.journalRepository = journalRepository;
    }

    @Override
    public void validateTarget(@NonNull Integer journalId) {
        if (!journalRepository.existsById(journalId)) {
            throw new NotFoundException("查询不到该日志信息").setErrorData(journalId);
        }
    }
}
