package org.jwebppy.platform.mgmt.upload_file.web;

import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.jwebppy.platform.mgmt.upload_file.service.UploadFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/upload_file")
public class UploadFileListController extends MgmtGeneralController
{
	@Autowired
	private UploadFileListService uploadFileListService;

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam("ufSeq") Integer ufSeq, @RequestParam("tSeq") Integer tSeq)
	{
		return uploadFileListService.findUploadFileLists(ufSeq, tSeq);
	}
}
