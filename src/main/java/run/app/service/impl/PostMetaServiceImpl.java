package run.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import run.app.exception.NotFoundException;
import run.app.model.entity.PostMeta;
import run.app.repository.PostRepository;
import run.app.repository.base.BaseMetaRepository;
import run.app.service.PostMetaService;

/**
 * Post meta service implementation class.
 *
 * @author ryanwang
 * @author ikaisec
 * @author guqing
 * @date 2019-08-04
 */
@Slf4j
@Service
public class PostMetaServiceImpl extends BaseMetaServiceImpl<PostMeta> implements PostMetaService {

    private final PostRepository postRepository;

    public PostMetaServiceImpl(BaseMetaRepository<PostMeta> baseMetaRepository,
        PostRepository postRepository) {
        super(baseMetaRepository);
        this.postRepository = postRepository;
    }

    @Override
    public void validateTarget(@NonNull Integer postId) {
        postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(postId));
    }
}
