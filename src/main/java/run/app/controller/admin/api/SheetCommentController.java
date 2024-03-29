package run.app.controller.admin.api;

import static org.springframework.data.domain.Sort.Direction.DESC;

import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import run.app.model.dto.BaseCommentDTO;
import run.app.model.entity.SheetComment;
import run.app.model.enums.CommentStatus;
import run.app.model.params.CommentQuery;
import run.app.model.params.SheetCommentParam;
import run.app.model.vo.BaseCommentVO;
import run.app.model.vo.BaseCommentWithParentVO;
import run.app.model.vo.SheetCommentWithSheetVO;
import run.app.service.OptionService;
import run.app.service.SheetCommentService;
import run.app.service.assembler.comment.SheetCommentAssembler;

/**
 * Sheet comment controller.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-04-25
 */
@RestController
@RequestMapping("/api/admin/sheets/comments")
public class SheetCommentController {

    private final SheetCommentAssembler sheetCommentAssembler;

    private final SheetCommentService sheetCommentService;

    private final OptionService optionService;

    public SheetCommentController(
        SheetCommentAssembler sheetCommentAssembler,
        SheetCommentService sheetCommentService,
        OptionService optionService) {
        this.sheetCommentAssembler = sheetCommentAssembler;
        this.sheetCommentService = sheetCommentService;
        this.optionService = optionService;
    }

    @GetMapping
    @ApiOperation("Lists sheet comments")
    public Page<SheetCommentWithSheetVO> pageBy(
        @PageableDefault(sort = "createTime", direction = DESC) Pageable pageable,
        CommentQuery commentQuery) {
        Page<SheetComment> sheetCommentPage = sheetCommentService.pageBy(commentQuery, pageable);
        return sheetCommentAssembler.convertToWithSheetVo(sheetCommentPage);
    }

    @GetMapping("latest")
    @ApiOperation("Lists latest sheet comments")
    public List<SheetCommentWithSheetVO> listLatest(
        @RequestParam(name = "top", defaultValue = "10") int top,
        @RequestParam(name = "status", required = false) CommentStatus status) {
        Page<SheetComment> sheetCommentPage = sheetCommentService.pageLatest(top, status);
        return sheetCommentAssembler.convertToWithSheetVo(sheetCommentPage.getContent());
    }

    @GetMapping("{sheetId:\\d+}/tree_view")
    @ApiOperation("Lists sheet comments with tree view")
    public Page<BaseCommentVO> listCommentTree(@PathVariable("sheetId") Integer sheetId,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
        return sheetCommentService
            .pageVosAllBy(sheetId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
    }

    @GetMapping("{sheetId:\\d+}/list_view")
    @ApiOperation("Lists sheet comment with list view")
    public Page<BaseCommentWithParentVO> listComments(@PathVariable("sheetId") Integer sheetId,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
        @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
        return sheetCommentService.pageWithParentVoBy(sheetId,
            PageRequest.of(page, optionService.getCommentPageSize(), sort));
    }

    @PostMapping
    @ApiOperation("Creates a sheet comment (new or reply)")
    public BaseCommentDTO createBy(@RequestBody SheetCommentParam commentParam) {
        SheetComment createdComment = sheetCommentService.createBy(commentParam);
        return sheetCommentAssembler.convertTo(createdComment);
    }

    @PutMapping("{commentId:\\d+}/status/{status}")
    @ApiOperation("Updates sheet comment status")
    public BaseCommentDTO updateStatusBy(@PathVariable("commentId") Long commentId,
        @PathVariable("status") CommentStatus status) {
        // Update comment status
        SheetComment updatedSheetComment = sheetCommentService.updateStatus(commentId, status);
        return sheetCommentAssembler.convertTo(updatedSheetComment);
    }

    @PutMapping("status/{status}")
    @ApiOperation("Updates sheet comment status in batch")
    public List<BaseCommentDTO> updateStatusInBatch(
        @PathVariable(name = "status") CommentStatus status,
        @RequestBody List<Long> ids) {
        List<SheetComment> comments = sheetCommentService.updateStatusByIds(ids, status);
        return sheetCommentAssembler.convertTo(comments);
    }


    @DeleteMapping("{commentId:\\d+}")
    @ApiOperation("Deletes sheet comment permanently and recursively")
    public BaseCommentDTO deletePermanently(@PathVariable("commentId") Long commentId) {
        SheetComment deletedSheetComment = sheetCommentService.removeById(commentId);
        return sheetCommentAssembler.convertTo(deletedSheetComment);
    }

    @DeleteMapping
    @ApiOperation("Deletes sheet comments permanently in batch by id array")
    public List<SheetComment> deletePermanentlyInBatch(@RequestBody List<Long> ids) {
        return sheetCommentService.removeByIds(ids);
    }

    @GetMapping("{commentId:\\d+}")
    @ApiOperation("Gets a sheet comment by comment id")
    public SheetCommentWithSheetVO getBy(@PathVariable("commentId") Long commentId) {
        SheetComment comment = sheetCommentService.getById(commentId);
        return sheetCommentAssembler.convertToWithSheetVo(comment);
    }

    @PutMapping("{commentId:\\d+}")
    @ApiOperation("Updates a sheet comment")
    public BaseCommentDTO updateBy(@Valid @RequestBody SheetCommentParam commentParam,
        @PathVariable("commentId") Long commentId) {
        SheetComment commentToUpdate = sheetCommentService.getById(commentId);

        commentParam.update(commentToUpdate);

        return sheetCommentAssembler.convertTo(sheetCommentService.update(commentToUpdate));
    }
}
