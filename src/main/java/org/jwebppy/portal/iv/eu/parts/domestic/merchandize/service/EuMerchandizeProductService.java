package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.dto.LangType;
import org.jwebppy.platform.mgmt.i18n.entity.LangDetailEntity;
import org.jwebppy.platform.mgmt.i18n.entity.LangEntity;
import org.jwebppy.platform.mgmt.i18n.mapper.LangMapper;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadDto;
import org.jwebppy.portal.iv.eu.common.file_upload.entity.FileUploadEntity;
import org.jwebppy.portal.iv.eu.common.file_upload.mapper.EuFileUploadMapper;
import org.jwebppy.portal.iv.eu.common.file_upload.service.EuFileUploadService;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeGeneralService;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeProductDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeProductSearchDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeLangRlEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeProductEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeRecommendProductEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper.EuMerchandizeLangRlMapper;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper.EuMerchandizeProductMapper;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper.EuMerchandizeRecommendProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EuMerchandizeProductService extends EuMerchandizeGeneralService
{
	@Autowired
	private EuMerchandizeProductMapper merchandizeProductMapper;
	@Autowired
	private EuMerchandizeLangRlMapper merchandizeLangRlMapper;
	@Autowired
	private LangMapper langMapper;
	@Autowired
	private EuMerchandizeRecommendProductMapper merchandizeRecommendProductMapper;
	@Autowired
	private EuFileUploadService fileUploadService;
	@Autowired
	private EuFileUploadMapper fileUploadMapper;

	@Autowired
	private Environment environment;


	// 상품 n건 취득
	public List<MerchandizeProductDto> getProductItems(MerchandizeProductSearchDto productSearchDto)
	{
		List<MerchandizeProductEntity> productEntities = merchandizeProductMapper.findProductItems(productSearchDto);
		return CmModelMapperUtils.mapAll(productEntities, MerchandizeProductDto.class);
	}

	// 상품 1건 취득
	public MerchandizeProductDto getProduct(MerchandizeProductDto productDto)
	{
		MerchandizeProductEntity productEntity = merchandizeProductMapper.findProductItem(productDto);
		return CmModelMapperUtils.map(productEntity, MerchandizeProductDto.class);
	}

	// 상품 1건 등록 또는 수정 분기
	@Transactional
	public int saveProduct(MerchandizeProductDto dto, FileUploadDto fuDto) throws Exception
	{
		int savePrdCnt = 0;

		// 수정
		if (CmStringUtils.isNotEmpty(dto.getMpSeq()))
		{
			MerchandizeProductEntity updateEntity = CmModelMapperUtils.map(dto, MerchandizeProductEntity.class);
			savePrdCnt = merchandizeProductMapper.updateProductItem(updateEntity);
			saveProductLangData(dto, updateEntity, EuMerchandizeCommonVo.UPDATE);

			// 파일 처리
			fileSaveProcess(dto, fuDto, updateEntity, EuMerchandizeCommonVo.UPDATE);

			return savePrdCnt;

		// 등록
		} else
		{
			// 상품코드 중복 체크
			MerchandizeProductEntity productEntity = merchandizeProductMapper.verifyProductItem(dto);
			if (productEntity == null || CmStringUtils.isEmpty(productEntity.getMaterialNo()))
			{
				MerchandizeProductEntity insertEntity = CmModelMapperUtils.map(dto, MerchandizeProductEntity.class);
				savePrdCnt = merchandizeProductMapper.insertProductItem(insertEntity);
				saveProductLangData(dto, insertEntity, EuMerchandizeCommonVo.INSERT);

				// 파일 처리
				fileSaveProcess(dto, fuDto, insertEntity, EuMerchandizeCommonVo.INSERT);

				return savePrdCnt;
			} else
			{
				return -1;	// 중복
			}
		}
	}

	/**
	 * 상품 이미지 파일 처리
	 *
	 * @param mpDto	상품 정보가 담긴 DTO
	 * @param fuDto	파일 정보가 담긴 DTO
	 * @param mpEntity	파일 업로드 시 t_seq를 넣어주기 위한 정보가 담긴 상품정보 DTO
	 * @param insertOrUpdate	삽입 or 갱신 판별 문자열
	 * @throws Exception
	 */
	private void fileSaveProcess(MerchandizeProductDto mpDto, FileUploadDto fuDto, MerchandizeProductEntity mpEntity, String insertOrUpdate) throws Exception
	{
		// 수정에서 호출되었을 경우 파일 공통 서비스 구조가 Delete-Insert이므로 상품 시퀀스 하나에 딸린 모든 삭제대상 파일을 삭제함.
		if (EuMerchandizeCommonVo.UPDATE.equals(insertOrUpdate))
		{
			// 1. 하나의 상품에 엮인 모든 이미지 파일 물리정보 및 논리정보 삭제
			fileUploadService.fileDelete(fuDto);

			// 2. 1번에서 삭제한 대상에는 썸네일 정보가 포함되어 있지 않으므로 썸네일 정보를 삭제
			// 2-1. 물리 삭제
			FileUploadDto deleteThumbnailDto = new FileUploadDto();
			deleteThumbnailDto.setTSeq(mpDto.getMpSeq());
			deleteThumbnailDto.setTarget(EuMerchandizeCommonVo.FILE_TARGET_MALL);
			deleteThumbnailDto.setType(EuMerchandizeCommonVo.FILE_TYPE_THUMBNAIL);

			List<FileUploadEntity> FileUploadEntities = fileUploadMapper.findFileUploads(deleteThumbnailDto);
			FileUploadEntity thumbnailEntity = FileUploadEntities.get(0);

			// 2.2. 썸네일 파일 논리 삭제
			FileUploadEntity fuDeleteEntity = new FileUploadEntity();
			fuDeleteEntity.setFuSeq(thumbnailEntity.getFuSeq());
			fuDeleteEntity.setCorp(mpDto.getCorp());
			fileUploadMapper.deleteEpFileUpload(fuDeleteEntity);
		}

		// 1. 일반 이미지 파일 생성 및 저장
		fuDto.setTarget(EuMerchandizeCommonVo.FILE_TARGET_MALL);
		fuDto.setTSeq(mpEntity.getMpSeq());

		fuDto.setFileStoragePath(getMerchandizeFileUploadPath());

		fuDto.setType(EuMerchandizeCommonVo.FILE_TYPE_IMAGE);
		fileUploadService.fileInsert(fuDto);

		// 2. 클라이언트에서 전송된 파일 중 가장 빠른 파일(썸네일을 만들어 저장할 파일) 정보를 취득
		FileUploadEntity thumbnailEntity = fileUploadMapper.findFuSeq(fuDto);
		thumbnailEntity.setStoragePath(environment.getProperty("file.upload.rootPath") + thumbnailEntity.getStoragePath());

		// 3. 썸네일 파일 생성 및 저장
		createThumbnailImage(thumbnailEntity, mpEntity);
	}

	// 다국어 정보 저장 또는 수정
	private void saveProductLangData(MerchandizeProductDto dto, MerchandizeProductEntity productEntity, String insertOrUpdate)
	{
		String[] elementLoopStrArray = {EuMerchandizeCommonVo.PRODUCT_NAME, EuMerchandizeCommonVo.PRODUCT_DESCRIPTION};

		// 상품명, 상품설명을 각각 지원하는 모든 다국어별로 저장
		for (String elementType: elementLoopStrArray)
		{
			if (EuMerchandizeCommonVo.INSERT.equals(insertOrUpdate))
			{
				insertLangInfo(dto, productEntity, elementType, new Boolean(false), 0);
			} else if (EuMerchandizeCommonVo.UPDATE.equals(insertOrUpdate))
			{

				for (int i = 0; i < dto.getProductLangKindSeqs().length; i++)
				{
					// 다국어 상세 식별자가 0보다 클 경우 정상 갱신, 0일 경우 상품이 추가된 다음에 지원 언어 자체가 추가된 케이스이므로 신규 삽입 처리
					if ( dto.getProductNameLangDetailSeqs()[i] > 0 )
					{
						LangDetailEntity ldEntity = new LangDetailEntity();

						if (elementType.equals(EuMerchandizeCommonVo.PRODUCT_NAME))
						{
							ldEntity.setLdSeq(dto.getProductNameLangDetailSeqs()[i]);
							ldEntity.setText(dto.getProductNames()[i]);
						} else if (elementType.equals(EuMerchandizeCommonVo.PRODUCT_DESCRIPTION))
						{
							ldEntity.setLdSeq(dto.getProductDescLangDetailSeqs()[i]);
							ldEntity.setText(dto.getProductDescs()[i]);
						}

						langMapper.updateLangDetail(ldEntity);		// 다국어 상세 저장

					} else if ( dto.getProductNameLangDetailSeqs()[i] == 0 )
					{
						insertLangInfo(dto, productEntity, elementType, new Boolean(true), i);
					}
				}
			}
		}
	}

	// 상품 삭제(삭제 플래그만 세움)
	@Transactional
	public int deleteProduct(List<Integer> seqs)
	{
		if (CollectionUtils.isNotEmpty(seqs))
		{
			int result = 0;
			for (Integer seq : seqs)
			{
				MerchandizeProductEntity productEntity = new MerchandizeProductEntity();
				productEntity.setMpSeq(seq);
				productEntity.setFgDelete(PlatformCommonVo.YES);

				// 상품 테이블(ep_mall_product) 데이터 삭제플래그 처리
				result += merchandizeProductMapper.updateProductItem(productEntity);
				// 추천상품 테이블(ep_mall_product) 데이터 삭제플래그 처리
				MerchandizeRecommendProductEntity reproductEntity = new MerchandizeRecommendProductEntity();
				reproductEntity.setMpSeq(seq);
				reproductEntity.setFgDelete(PlatformCommonVo.YES);
				result += merchandizeRecommendProductMapper.updateRecommendProductItem(reproductEntity);

				// 상품명, 상품설명 데이터 삭제플래그 처리
				String[] elementLoopStrArray = {EuMerchandizeCommonVo.PRODUCT_NAME, EuMerchandizeCommonVo.PRODUCT_DESCRIPTION};
				for (String elementType : elementLoopStrArray)
				{
					MerchandizeLangRlEntity mlrEntity = new MerchandizeLangRlEntity();
					mlrEntity.setTSeq(productEntity.getMpSeq());
					mlrEntity.setType(elementType);
					List<MerchandizeLangRlEntity> deleteMlrEntities = merchandizeLangRlMapper.findLangDetailSeqsForDelete(mlrEntity);

					for (MerchandizeLangRlEntity mlrEntityDel : deleteMlrEntities)
					{
						// 다국어 상세 테이블(pltf_lang_detail) 데이터 삭제플래그 처리
						merchandizeLangRlMapper.deleteLangDetail(mlrEntityDel);

						// 다국어 테이블(pltf_lang_detail) 데이터 삭제플래그 처리
						LangDetailEntity ldEntity = new LangDetailEntity();
						ldEntity.setLdSeq(mlrEntityDel.getLdSeq());
						merchandizeLangRlMapper.deleteLang(ldEntity);
					}
				}

				// 상품에 딸린 파일정보 삭제
				FileUploadDto deleteFileDto = new FileUploadDto();
				deleteFileDto.setTSeq(seq);
				deleteFileDto.setTarget(EuMerchandizeCommonVo.FILE_TARGET_MALL);

				List<FileUploadEntity> FileUploadEntities = fileUploadMapper.findFileUploads(deleteFileDto);
				for (FileUploadEntity delEntity : FileUploadEntities)
				{
					// 리얼 파일 삭제
					fileUploadService.realFileDelete(delEntity.getFuSeq());
				}
				// 파일 논리정보 삭제(이미지, 썸네일 모두)
				FileUploadEntity fuDeleteEntity = new FileUploadEntity();
				fuDeleteEntity.setTSeq(seq);
				fuDeleteEntity.setTarget(EuMerchandizeCommonVo.FILE_TARGET_MALL);
				fileUploadMapper.deleteFileUploadImageMerchandize(fuDeleteEntity);
			}
			return result;
		}
		return 0;
	}


	// 상품 n건 취득(사용자)
	public List<MerchandizeProductDto> getProductItemsUser(MerchandizeProductSearchDto productSearchDto)
	{
		List<MerchandizeProductEntity> productEntities = merchandizeProductMapper.findProductItemsUser(productSearchDto);
		return CmModelMapperUtils.mapAll(productEntities, MerchandizeProductDto.class);
	}


	// 다국어 정보 신규 삽입(신규 저장할 경우, 기존정보에서 다국어 추가로 인한 기존정보수정과 신규저장을 병행할 경우 2가지의 경우의 수가 있어 분리함)
	/**
	 *
	 * @param dto 신규 삽입 또는 수정 정보가 담긴 dto
	 * @param productEntity 상품 테이블에 저장을 마치고 해당 테이블의 저장된 데이터 식별자를 가지고 있는 상품Entity
	 * @param elementType	상품명, 상품설명을 구분하는 판별용 변수
	 * @param isUpdatePartFlag	신규 삽입 건일 경우 false, 수정 화면에서 조작한 수정 정보 처리 중 신규 다국어 정보를 삽입해야 할 경우에 호출되었을 경우 true
	 * @param lkCount	현재 지원하고 있는 다국어 중 몇번째 순서의 다국어인지 판별하는 변수(ex: 과거에 지원하는 다국어가 2개였을 경우 저장된 데이터를 수정할 때 현재 지원하는 다국어가 3개라면, 다국어 리스트 배열의 3번째에 해당하는 숫자)
	 */
	private void insertLangInfo(MerchandizeProductDto dto, MerchandizeProductEntity productEntity, String elementType, boolean isUpdatePartFlag, int lkCount) {
		LangEntity langEntity = new LangEntity();
		langEntity.setCorp(dto.getCorp());
		langEntity.setFgDelete(PlatformCommonVo.NO);
		//langEntity.setType("TXT");
		LangType.valueOf("TEXT");

		langMapper.insertLang(langEntity);	// 다국어 마스터 저장

		int lSeq = langEntity.getLSeq();
		if (lSeq > 0)
		{
			LangDetailEntity ldEntity = null;

			for (int i = 0; i < dto.getProductLangKindSeqs().length; i++)
			{
				// 수정 화면 처리에서 호출되었을 경우, 해당 다국어를 insert하는 경우가 아니면 pass하도록 처리
				if (isUpdatePartFlag)
				{
					if (i < lkCount)
					{
						continue;
					}
				}

				ldEntity = new LangDetailEntity();
				ldEntity.setLSeq(lSeq);
				ldEntity.setFgDelete(PlatformCommonVo.NO);
				ldEntity.setLkSeq(dto.getProductLangKindSeqs()[i]);

				if (elementType.equals(EuMerchandizeCommonVo.PRODUCT_NAME))
				{
					ldEntity.setText(dto.getProductNames()[i]);
				} else if (elementType.equals(EuMerchandizeCommonVo.PRODUCT_DESCRIPTION))
				{
					ldEntity.setText(dto.getProductDescs()[i]);
				}

				langMapper.insertLangDetail(ldEntity);		// 다국어 상세 저장

				MerchandizeLangRlEntity mlrEntity = null;
				int ldSeq = ldEntity.getLdSeq();
				if (ldSeq > 0)
				{
					mlrEntity = new MerchandizeLangRlEntity();
					mlrEntity.setLdSeq(ldSeq);
					mlrEntity.setTSeq(productEntity.getMpSeq());
					mlrEntity.setType(elementType);

					merchandizeLangRlMapper.insertItemDescription(mlrEntity);	// 머천다이즈 다국어 관계 저장
				}
			}
		}
	}
}
