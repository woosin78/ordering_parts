package org.jwebppy.platform.mgmt.content.web;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.cache.CacheClear;
import org.jwebppy.platform.core.util.CmAnnotationUtils;
import org.jwebppy.platform.core.util.CmClassUtils;
import org.jwebppy.platform.core.util.CmReflectionUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.mgmt.content.ContentGeneralController;
import org.jwebppy.platform.mgmt.content.dto.CItemComponentDto;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemLangRlDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentLangService;
import org.jwebppy.platform.mgmt.content.service.ContentService;
import org.jwebppy.platform.mgmt.i18n.dto.LangDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/content")
public class ContentController extends ContentGeneralController
{
	@Autowired
	private ContentLangService contentLangService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private LangService langService;

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/popup/view")
	public String viewPopup(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	public Object listLayout(@ModelAttribute CItemSearchDto citemSearch)
	{
		return contentService.getCitems(citemSearch);
	}

	@GetMapping("/layout/{tabPath}")
	@ResponseBody
	public Object viewLayout(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemSearchDto citemSearch)
	{
		if ("general".equals(tabPath))
		{
			CItemDto citem = (citemSearch.getCseq() == null) ? contentService.getRoot() : contentService.getCitem(citemSearch.getCseq());

			return ContentLayoutBuilder.viewGeneralInfo(citem);
		}
		else
		{
			List<CItemLangRlDto> citemLangRls = contentLangService.getCitemLangRls(CItemLangRlDto.builder()
					.cseq(citemSearch.getCseq())
					.basename(citemSearch.getBasename())
					.build());

			if (CollectionUtils.isNotEmpty(citemLangRls))
			{
				LangDto lang = langService.getLangByLSeq(citemLangRls.get(0).getLseq());

				return ContentLayoutBuilder.viewLang(langService.getBasenames(), langService.getLangKinds(LangKindDto.builder()
						.basename(lang.getBasename())
						.build()), lang);
			}
		}

		return Document.EMPTY;
	}

	@GetMapping("/write/layout/{tabPath}")
	@ResponseBody
	public Object writeLayout(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemSearchDto citemSearch)
	{
		CItemDto citem = (citemSearch.getCseq() != null) ? contentService.getCitem(citemSearch.getCseq()) : new CItemDto();
		CItemDto parentCItem = (citemSearch.getPseq() != null) ? contentService.getCitem(citemSearch.getPseq()) : new CItemDto();

		return ContentLayoutBuilder.writeGeneralInfo(citem, parentCItem,  getComponents(), getEntryPoints(citem.getComponent()));
	}

	@PostMapping("/save")
	@CacheClear(name = "A")
	@ResponseBody
	public Object save(@ModelAttribute CItemDto citem)
	{
		citem.setName(citem.getName().toUpperCase());

		return contentService.save(citem);
	}

	@PostMapping("/delete")
	@CacheClear(name = "A")
	@ResponseBody
	public Object delete(@RequestParam("cseq") Integer cseq)
	{
		return contentService.delete(cseq);
	}

	@PostMapping("/copy")
	@CacheClear(name = "A")
	@ResponseBody
	public Object copy(@RequestParam("cseq") Integer cseq, @RequestParam("pseq") Integer pseq)
	{
		return contentService.copy(cseq, pseq, MgmtCommonVo.NO);
	}

	@PostMapping("/copy_with_sub_items")
	@CacheClear(name = "A")
	@ResponseBody
	public Object copyWithSubItems(@RequestParam("cseq") Integer cseq, @RequestParam("pseq") Integer pseq)
	{
		return contentService.copy(cseq, pseq, MgmtCommonVo.YES);
	}

	@PostMapping("/move")
	@CacheClear(name = "A")
	@ResponseBody
	public Object move(@RequestParam("cseq") Integer cseq, @RequestParam("pseq") Integer pseq)
	{
		return contentService.move(cseq, pseq);
	}

	@PostMapping("/lang/save")
	@CacheClear(name = "A")
	@ResponseBody
	public Object saveLang(@ModelAttribute CItemLangRlDto citemLangRl)
	{
		return contentLangService.save(citemLangRl);
	}

	@GetMapping("/lang")
	@ResponseBody
	public Object lang(@ModelAttribute CItemLangRlDto pCItemLangRl)
	{
		List<CItemLangRlDto> citemLangRls = contentLangService.getCitemLangRls(pCItemLangRl);

		if (CollectionUtils.isNotEmpty(citemLangRls))
		{
			return langService.getLangByLSeq(citemLangRls.get(0).getLseq());
		}

		return null;
	}

	@GetMapping("/tree")
	@ResponseBody
	public Object itemsHierarchy(@ModelAttribute CItemSearchDto citemSearch)
	{
		if (ObjectUtils.isEmpty(citemSearch.getCseq()))
		{
			citemSearch.setCseq(contentService.getRoot().getCseq());
		}

		return contentService.getCitemHierarchy2(citemSearch);
	}

	@GetMapping("/component/entry_points")
	@ResponseBody
	public Object entryPoints(@ModelAttribute CItemSearchDto citemSearch)
	{
		return getEntryPoints(citemSearch.getComponent());
	}

	@GetMapping("/valid_check/{field}")
	@ResponseBody
	public Object validCheck(@PathVariable("field") String field, @RequestParam("value") String value, @RequestParam(required = false, value = "cseq") Integer cseq)
	{
		CItemSearchDto citemSearch = new CItemSearchDto();
		citemSearch.setName(value.toUpperCase());

		List<CItemDto> citems = contentService.getCitems(citemSearch);

		if (CollectionUtils.isEmpty(citems))
		{
			return EMPTY_RETURN_VALUE;
		}

		if (cseq != null)
		{
			for (CItemDto citem: citems)
			{
				if (cseq.equals(citem.getCseq()))
				{
					return EMPTY_RETURN_VALUE;
				}
			}
		}

		return "duplicated";
	}

	private List<CItemComponentDto> getComponents()
	{
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Controller.class));

		List<CItemComponentDto> itemComponents = new LinkedList<>();

		for (BeanDefinition beanDefinition : scanner.findCandidateComponents("org.jwebppy"))
		{
		    try
		    {
		    	Class<?> clazz = CmClassUtils.getClass(beanDefinition.getBeanClassName());

				itemComponents.add(CItemComponentDto.builder()
						.className(clazz.getName())
						.build());
			}
		    catch (ClassNotFoundException e)
		    {
				e.printStackTrace();
			}
		}

		return itemComponents;
	}

	private List<CItemComponentDto> getEntryPoints(String component)
	{
		if (CmStringUtils.isNotEmpty(component))
		{
			Class<?> clazz = null;

			try
			{
				clazz = CmClassUtils.getClass(component);
			}
			catch (ClassNotFoundException e)
			{
				return null;
			}

			Method[] methods = CmReflectionUtils.getAllDeclaredMethods(clazz);

			if (ObjectUtils.isEmpty(methods))
			{
				return null;
			}

			List<CItemComponentDto> citemComponents = new LinkedList<>();

    		for (Method method : methods)
    		{
    			if (ObjectUtils.isEmpty(method.getAnnotations()))
    			{
    				continue;
    			}

    			if (method.isAnnotationPresent(RequestMapping.class) && !method.isAnnotationPresent(ResponseBody.class))
    			{
					String url = getReturnUrl(clazz, method);

					if (CmStringUtils.isNotEmpty(url))
					{
						citemComponents.add(CItemComponentDto.builder()
								.className(clazz.getName())
								.methodName(method.getName())
								.url(url)
								.build());
					}
    			}
	    	}

    		return citemComponents;
		}

		return null;
	}

	protected String getReturnUrl(Class<?> clazz, Method method)
	{
		if (CmAnnotationUtils.isAnnotationDeclaredLocally(RequestMapping.class, clazz))
		{
	        String value1 = CmStringUtils.trimToEmpty(CmAnnotationUtils.getAnnotationValue(CmAnnotationUtils.getAnnotation(clazz, RequestMapping.class)));
			String value2 = CmStringUtils.trimToEmpty(CmAnnotationUtils.getAnnotationValue(CmAnnotationUtils.getAnnotation(method, RequestMapping.class)));

			return value1 + value2;
		}

		return null;
	}
}
