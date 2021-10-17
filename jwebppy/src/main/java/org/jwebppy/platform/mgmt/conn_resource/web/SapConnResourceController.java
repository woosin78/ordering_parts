package org.jwebppy.platform.mgmt.conn_resource.web;

import java.util.List;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceSearchDto;
import org.jwebppy.platform.mgmt.conn_resource.service.SapConnResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/conn_resource")
public class SapConnResourceController extends MgmtGeneralController
{
	@Autowired
	private SapConnResourceService sapConnResourceService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	public Object listLayout(@ModelAttribute SapConnResourceSearchDto sapConnResourceSearch)
	{
		return SapConnResourceLayoutBuilder.pageableList(new PageableList<>(sapConnResourceService.getPageableSapConnResources(sapConnResourceSearch)));
	}

	@GetMapping("/view")
	public Object view(Model model, WebRequest webRequest)
	{
		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/view/layout")
	@ResponseBody
	public Object viewLayout(@RequestParam("scrSeq") Integer scrSeq)
	{
		return SapConnResourceLayoutBuilder.view(sapConnResourceService.getSapConnResource(scrSeq));
	}

	@GetMapping("/write")
	public Object write(Model model, WebRequest webRequest, @RequestParam(required = false, name="scrSeq") Integer scrSeq)
	{
		SapConnResourceDto sapConnResource = (scrSeq == null) ? new SapConnResourceDto() : sapConnResourceService.getSapConnResource(scrSeq);

		model.addAttribute("sapConnResource", sapConnResource);

		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/write/layout")
	@ResponseBody
	public Object writeLayout(@ModelAttribute SapConnResourceDto sapConnResource)
	{
		Integer scrSeq = sapConnResource.getScrSeq();

		if (scrSeq != null)
		{
			sapConnResource = sapConnResourceService.getSapConnResource(scrSeq);
		}

		return SapConnResourceLayoutBuilder.write(sapConnResource);
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute SapConnResourceDto sapConnResource)
	{
		return sapConnResourceService.save(sapConnResource);
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("scrSeq") List<Integer> scrSeqs)
	{
		return sapConnResourceService.delete(scrSeqs);
	}

	@PostMapping("/use")
	@ResponseBody
	public Object use(@RequestParam("scrSeq") List<Integer> scrSeqs)
	{
		if (sapConnResourceService.modifyFgUse(scrSeqs, PlatformCommonVo.YES) > 0)
		{
			return PlatformCommonVo.SUCCESS;
		}

		return PlatformCommonVo.FAIL;
	}

	@PostMapping("/disuse")
	@ResponseBody
	public Object disuse(@RequestParam("scrSeq") List<Integer> scrSeqs)
	{
		if (sapConnResourceService.modifyFgUse(scrSeqs, PlatformCommonVo.NO) > 0)
		{
			return PlatformCommonVo.SUCCESS;
		}

		return PlatformCommonVo.FAIL;
	}
}
