package org.jwebppy.platform.mgmt.content.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmAnnotationUtils;
import org.jwebppy.platform.core.util.CmClassUtils;
import org.jwebppy.platform.core.util.CmReflectionUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.mgmt.content.ContentGeneralController;
import org.jwebppy.platform.mgmt.content.dto.CItemComponentDto;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemLangRlDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/content")
public class ContentController extends ContentGeneralController
{
	@Autowired
	private ContentAuthorityService contentAuthorityService;

	@Autowired
	private ContentLangService contentLangService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private LangService langService;

	@RequestMapping("/main")
	public String main()
	{
		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute CItemSearchDto cItemSearch)
	{
		return contentService.getCItems(cItemSearch);
	}

	@GetMapping("/{tabPath}")
	@ResponseBody
	public Object view(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemSearchDto cItemSearch)
	{
		if ("general".equals(tabPath))
		{
			CItemDto cItem = (cItemSearch.getCSeq() == null) ? contentService.getRoot() : contentService.getCItem(cItemSearch.getCSeq());

			return ContentLayoutBuilder.getGeneralInfo(cItem);
		}
		else
		{
			CItemLangRlDto cItemLangRl = new CItemLangRlDto();
			cItemLangRl.setCSeq(cItemSearch.getCSeq());
			cItemLangRl.setBasename(cItemSearch.getBasename());

			List<CItemLangRlDto> cItemLangRls = contentLangService.getLangs(cItemLangRl);

			if (CollectionUtils.isNotEmpty(cItemLangRls))
			{
				LangDto lang = langService.getLangByLSeq(cItemLangRls.get(0).getLSeq());

				LangKindDto langKind = new LangKindDto();
				langKind.setBasename(lang.getBasename());

				return ContentLayoutBuilder.getLang(langService.getBasenames(), langService.getLangKinds(langKind), lang);
			}
		}

		return Document.EMPTY;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute CItemDto cItem)
	{
		cItem.setName(cItem.getName().toUpperCase());

		return contentService.save(cItem);
	}

	@GetMapping("/modify/{tabPath}")
	@ResponseBody
	public Object modify(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemSearchDto cItemSearch)
	{
		CItemDto cItem = new CItemDto();

		if (cItemSearch.getCSeq() != null)
		{
			cItem = contentService.getCItem(cItemSearch.getCSeq());
		}

		return ContentLayoutBuilder.getGeneralInfoForm(cItem, getComponents(), getEntryPoints(cItem.getComponent()));
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("cSeq") Integer cSeq)
	{
		return contentService.delete(cSeq);
	}

	@PostMapping("/copy")
	@ResponseBody
	public Object copy(@RequestParam("cSeq") Integer cSeq, @RequestParam("pSeq") Integer pSeq)
	{
		return contentService.copy(cSeq, pSeq, PlatformCommonVo.NO);
	}

	@PostMapping("/copy_with_sub_items")
	@ResponseBody
	public Object copyWithSubItems(@RequestParam("cSeq") Integer cSeq, @RequestParam("pSeq") Integer pSeq)
	{
		return contentService.copy(cSeq, pSeq, PlatformCommonVo.YES);
	}

	@PostMapping("/move")
	@ResponseBody
	public Object move(@RequestParam("cSeq") Integer cSeq, @RequestParam("pSeq") Integer pSeq)
	{
		return contentService.move(cSeq, pSeq);
	}

	@PostMapping("/lang/save")
	@ResponseBody
	public Object saveLang(@ModelAttribute CItemLangRlDto cItemLangRl)
	{
		return contentLangService.save(cItemLangRl);
	}

	@GetMapping("/lang")
	@ResponseBody
	public Object lang(@ModelAttribute CItemLangRlDto pCItemLangRl)
	{
		List<CItemLangRlDto> citemLangRls = contentLangService.getLangs(pCItemLangRl);

		if (CollectionUtils.isNotEmpty(citemLangRls))
		{
			return langService.getLangByLSeq(citemLangRls.get(0).getLSeq());
		}

		return null;
	}

	@GetMapping("/tree")
	@ResponseBody
	public Object itemsHierarchy(@ModelAttribute CItemSearchDto cItemSearch)
	{
		if (cItemSearch.getCSeq() == null)
		{
			cItemSearch.setCSeq(contentService.getRoot().getCSeq());
		}

		return contentService.getCItemHierarchy2(cItemSearch);
	}

	@GetMapping("/component/entry_points")
	@ResponseBody
	public Object entryPoints(@ModelAttribute CItemSearchDto cItemSearch)
	{
		return getEntryPoints(cItemSearch.getComponent());
	}

	@GetMapping("/valid_check/{field}")
	@ResponseBody
	public Object validCheck(@PathVariable("field") String field, @RequestParam("value") String value, @RequestParam(required = false, value = "cSeq") Integer cSeq)
	{
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setName(value.toUpperCase());

		List<CItemDto> cItems = contentService.getCItems(cItemSearch);

		if (CollectionUtils.isEmpty(cItems))
		{
			return EMPTY_RETURN_VALUE;
		}

		if (cSeq != null)
		{
			for (CItemDto cItem: cItems)
			{
				if (cSeq.equals(cItem.getCSeq()))
				{
					return EMPTY_RETURN_VALUE;
				}
			}
		}

		return "duplicated";
	}

	@GetMapping("/my_menu")
	@ResponseBody
	public Object myMenu(@ModelAttribute CItemSearchDto cItemSearch)
	{
		List<Map<String, Object>> cItemsHierarchy = new LinkedList<>();

		cItemSearch.setUSeq(getUSeq());

		List<CItemDto> cItems = contentAuthorityService.getMyCItemHierarchy(cItemSearch);

		for (CItemDto cItem : cItems)
		{
			Map<String, Object> itemMap = new LinkedHashMap<>();
			itemMap.put("KEY", cItem.getCSeq());
			itemMap.put("NAME", CmStringUtils.defaultIfEmpty(cItem.getName2(), cItem.getName()));
			itemMap.put("TYPE", cItem.getType().toString());
			itemMap.put("URL", CmStringUtils.trimToEmpty(cItem.getUrl()));

			itemMap.put("SUB_ITEMS", getSubItems(cItem.getSubCItems()));

			cItemsHierarchy.add(itemMap);
		}

		return cItemsHierarchy;
	}

	protected List<Map<String, Object>> getSubItems(List<CItemDto> subCItems)
	{
		List<Map<String, Object>> cItems = new LinkedList<>();

		if (CollectionUtils.isNotEmpty(subCItems))
		{
			for (CItemDto subCItem: subCItems)
			{
				Map<String, Object> itemMap = new LinkedHashMap<>();
				itemMap.put("KEY", subCItem.getCSeq());
				itemMap.put("NAME", langService.getCItemText(PlatformCommonVo.DEFAULT_BASENAME, subCItem.getCSeq(), UserAuthenticationUtils.getUserDetails().getLanguage()));
				itemMap.put("TYPE", subCItem.getType().toString());
				itemMap.put("URL", subCItem.getUrl());
				itemMap.put("SUB_ITEMS", getSubItems(subCItem.getSubCItems()));

				cItems.add(itemMap);
			}
		}

		return cItems;
	}

	/*
	@GetMapping("/breadcrumb")
	@ResponseBody
	public Object breadcrumb(CItemSearchDto cItemSearch)
	{
		cItemSearch.setUSeq(getUSeq());

		List<CItemDto> cItems = contentAuthorityService.getMyItemHierarchy(cItemSearch);

		if (CollectionUtils.isNotEmpty(cItems))
		{
			for (CItemDto cItem: cItems)
			{
				List<CItemDto> breadcrumb = new ArrayList<>();

				if (isFindEntryPoint(breadcrumb, cItem, cItemSearch.getEntryPoint()))
				{
					Collections.reverse(breadcrumb);

					List<CItemDto> breadcrumb2 = new ArrayList<>();
					breadcrumb2.add(breadcrumb.get(0));

					for (int i=0, size=breadcrumb.size(); i<size; i++)
					{
						CItemDto cItem2 = breadcrumb.get(i);

						for (int j=i; j<size; j++)
						{
							CItemDto cItems3 = breadcrumb.get(j);

							if (cItem2.getPSeq().equals(cItems3.getCSeq()))
							{
								breadcrumb2.add(cItems3);
							}
						}
					}

					return breadcrumb2;
				}
			}
		}

		return null;
	}

	private boolean isFindEntryPoint(List<CItemDto> breadcrumb, CItemDto cItem, String url)
	{
		if (CollectionUtils.isNotEmpty(cItem.getSubCItems()))
		{
			for (CItemDto subCItem: cItem.getSubCItems())
			{
				if (!subCItem.getType().equals(CItemType.M) && !subCItem.getType().equals(CItemType.P))
				{
					continue;
				}

				if (subCItem.getType().equals(CItemType.P) && CmStringUtils.notEquals(url, subCItem.getEntryPoint()))
				{
					continue;
				}

				breadcrumb.add(subCItem);

				if (CmStringUtils.equals(url, subCItem.getEntryPoint()))
				{
					return true;
				}

				if (isFindEntryPoint(breadcrumb, subCItem, url))
				{
					return true;
				}
			}
		}

		return false;
	}
	*/

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

				CItemComponentDto cItemComponent = new CItemComponentDto();
				cItemComponent.setClassName(clazz.getName());

				itemComponents.add(cItemComponent);
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

			if (methods == null)
			{
				return null;
			}

			List<CItemComponentDto> cItemComponents = new LinkedList<>();

    		for (Method method : methods)
    		{
    			Annotation[] annotations = method.getAnnotations();

    			if (annotations == null)
    			{
    				continue;
    			}

    			if (!method.isAnnotationPresent(ResponseBody.class))
    			{
					String url = getReturnUrl(clazz, method);

					if (CmStringUtils.isNotEmpty(url))
					{
						CItemComponentDto cItemComponent = new CItemComponentDto();
						cItemComponent.setClassName(clazz.getName());
						cItemComponent.setMethodName(method.getName());
						cItemComponent.setUrl(url);

						cItemComponents.add(cItemComponent);
					}
    			}
	    	}

    		return cItemComponents;
		}

		return null;
	}

	protected String getReturnUrl(Class<?> clazz, Method method)
	{
		if (CmAnnotationUtils.isAnnotationDeclaredLocally(RequestMapping.class, clazz))
		{
	        String value1 = CmAnnotationUtils.getAnnotationValue(CmAnnotationUtils.getAnnotation(clazz, RequestMapping.class));
			String value2 = CmAnnotationUtils.getAnnotationValue(CmAnnotationUtils.getAnnotation(method, RequestMapping.class));

			if (CmStringUtils.isNotEmpty(value1) && CmStringUtils.isNotEmpty(value2))
			{
				return value1 + value2;
			}
		}

		return null;
	}
}
