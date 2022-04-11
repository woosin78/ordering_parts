package org.jwebppy.portal.iv.upload.web;

import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.upload.service.EpUploadFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH + "/upload")
public class EpUploadFileListController extends IvGeneralController
{
	@Autowired
	private EpUploadFileListService uploadFileListService;

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam("ufSeq") String ufSeq, @RequestParam("tSeq") String tSeq)
	{
		return uploadFileListService.getUploadFileLists(ufSeq, tSeq);
	}
}
