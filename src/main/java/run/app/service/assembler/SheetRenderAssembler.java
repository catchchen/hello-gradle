package run.app.service.assembler;

import java.util.List;
import org.springframework.stereotype.Component;
import run.app.model.entity.Content;
import run.app.model.entity.Content.PatchedContent;
import run.app.model.entity.Sheet;
import run.app.model.entity.SheetMeta;
import run.app.model.vo.SheetDetailVO;
import run.app.service.ContentPatchLogService;
import run.app.service.ContentService;
import run.app.service.OptionService;
import run.app.service.SheetCommentService;
import run.app.service.SheetMetaService;

/**
 * Sheet assembler for theme render.
 *
 * @author guqing
 * @date 2022-03-01
 */
@Component
public class SheetRenderAssembler extends SheetAssembler {

    private final SheetMetaService sheetMetaService;
    private final ContentService contentService;
    private final ContentPatchLogService contentPatchLogService;

    public SheetRenderAssembler(SheetCommentService sheetCommentService,
        ContentService contentService,
        SheetMetaService sheetMetaService,
        OptionService optionService,
        ContentPatchLogService contentPatchLogService) {
        super(sheetCommentService, contentService, sheetMetaService, optionService);
        this.sheetMetaService = sheetMetaService;
        this.contentService = contentService;
        this.contentPatchLogService = contentPatchLogService;
    }

    @Override
    public SheetDetailVO convertToDetailVo(Sheet sheet) {
        sheet.setContent(Content.PatchedContent.of(contentService.getById(sheet.getId())));
        // List metas
        List<SheetMeta> metas = sheetMetaService.listBy(sheet.getId());
        // Convert to detail vo
        return super.convertTo(sheet, metas);
    }

    /**
     * Gets sheet detail to preview.
     *
     * @param sheet sheet
     * @return sheet detail with the latest patched content.
     */
    public SheetDetailVO convertToPreviewDetailVo(Sheet sheet) {
        sheet.setContent(getLatestContentBy(sheet.getId()));
        // List metas
        List<SheetMeta> metas = sheetMetaService.listBy(sheet.getId());
        // Convert to detail vo
        return super.convertTo(sheet, metas);
    }

    private PatchedContent getLatestContentBy(Integer postId) {
        Content postContent = contentService.getById(postId);
        // Use the head pointer stored in the post content.
        return contentPatchLogService.getPatchedContentById(postContent.getHeadPatchLogId());
    }
}