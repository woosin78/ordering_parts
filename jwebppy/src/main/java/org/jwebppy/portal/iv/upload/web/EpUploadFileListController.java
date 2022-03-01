package org.jwebppy.portal.iv.upload.web;

import org.jwebppy.platform.mgmt.upload.service.UploadFileListService;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
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
	private UploadFileListService uploadFileListService;

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam("ufSeq") Integer ufSeq, @RequestParam("tSeq") Integer tSeq)
	{
		return uploadFileListService.getUploadFileLists(ufSeq, tSeq);
	}
}
