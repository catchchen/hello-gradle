package run.app.event.post;

import org.springframework.context.ApplicationEvent;
import run.app.model.entity.Post;

/**
 * Post updated event.
 *
 * @author guqing
 * @date 2022-02-24
 */
public class PostUpdatedEvent extends ApplicationEvent {

    private final Post post;

    public PostUpdatedEvent(Object source, Post post) {
        super(source);
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}
