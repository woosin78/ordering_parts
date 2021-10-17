package org.jwebppy.platform.mgmt.upload.web;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.jwebppy.platform.mgmt.upload.service.UploadFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/upload")
public class UploadFileListController extends MgmtGeneralController
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
