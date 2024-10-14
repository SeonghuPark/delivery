package org.delivery.api.domain.userorder.business;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.userorder.UserOrderEntity;

import java.util.List;
import java.util.stream.Collectors;

@Business
@RequiredArgsConstructor
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final StoreMenuService storeMenuService;
    private final UserOrderConverter userOrderConverter;
    private final UserOrderMenuConverter userOrderMenuConverter;
    private final UserOrderMenuService userOrderMenuService;
    private final StoreService storeService;
    private final StoreMenuConverter storeMenuConverter;
    private final StoreConverter storeConverter;

    // 사용자, 메뉴 id
    // userOrder 생성
    // userOrderMenu 생성
    // 응답생성
    public UserOrderResponse userOrder(User user, UserOrderRequest body) {
        var storeMenuEntityList = body.getStoreMenuIdList().stream()
                .map(storeMenuService::getStoreMenuWithThrow)
                .toList();

        var userOrderEntity = userOrderConverter.toEntity(user, storeMenuEntityList);

        // 주문
        var newUserOrderEntity = userOrderService.order(userOrderEntity);

        // 맵핑
        var userOrderMenuEntityList = storeMenuEntityList.stream()
                .map(it -> {
                    var userOrderMenuEntity = userOrderMenuConverter.toEntity(newUserOrderEntity, it);
                    return userOrderMenuEntity;
                })
                .toList();

        // 주문내역기록
        userOrderMenuEntityList.forEach(userOrderMenuService::order);

        // response
        return userOrderConverter.toResponse(newUserOrderEntity);

    }

    public List<UserOrderDetailResponse> current(User user) {
        var userOrderEntityList = userOrderService.current(user.getId());

        // 주문 1건씩 처리
        var userOrderDetailResponseList = userOrderEntityList.stream()
                .map(it -> {
                    // 사용자가 주문한 메뉴
                    var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenus(it.getId());

                    var storeMenuEntityList = userOrderMenuEntityList.stream()
                            .map(userOrderMenuEntity -> {
                                var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                                return storeMenuEntity;
                            })
                            .toList();

                    // 사용자가 주문한 스토어 TODO 리팩토링 필요
                    var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

                    return UserOrderDetailResponse.builder()
                            .userOrderResponse(userOrderConverter.toResponse(it))
                            .storeMenuResponsesList(storeMenuConverter.toResponse(storeMenuEntityList))
                            .storeResponse(storeConverter.toResponse(storeEntity))
                            .build();
                })
                .toList();

        return userOrderDetailResponseList;

    }

    public List<UserOrderDetailResponse> history(User user) {
        var userOrderEntityList = userOrderService.history(user.getId());

        // 주문 1건씩 처리
        var userOrderDetailResponseList = userOrderEntityList.stream()
                .map(it -> {
                    // 사용자가 주문한 메뉴
                    var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenus(it.getId());

                    var storeMenuEntityList = userOrderMenuEntityList.stream()
                            .map(userOrderMenuEntity -> {
                                var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                                return storeMenuEntity;
                            })
                            .toList();

                    // 사용자가 주문한 스토어 TODO 리팩토링 필요
                    var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

                    return UserOrderDetailResponse.builder()
                            .userOrderResponse(userOrderConverter.toResponse(it))
                            .storeMenuResponsesList(storeMenuConverter.toResponse(storeMenuEntityList))
                            .storeResponse(storeConverter.toResponse(storeEntity))
                            .build();
                })
                .toList();

        return userOrderDetailResponseList;
    }

    public UserOrderDetailResponse read(User user, Long orderId) {

        var userOrderEntity = userOrderService.getUserOrderWithOutStatusWithThrow(orderId, user.getId());

        //사용자가 주문한 메뉴
        var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenus(userOrderEntity.getId());

        var storeMenuEntityList = userOrderMenuEntityList.stream()
                .map(userOrderMenuEntity -> {
                    var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                    return storeMenuEntity;
                })
                .toList();

        // 사용자가 주문한 스토어 TODO 리팩토링 필요
        var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStoreId());

        return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
                .storeMenuResponsesList(storeMenuConverter.toResponse(storeMenuEntityList))
                .storeResponse(storeConverter.toResponse(storeEntity))
                .build();
    }
}
