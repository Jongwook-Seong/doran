(globalThis.TURBOPACK || (globalThis.TURBOPACK = [])).push([typeof document === "object" ? document.currentScript : undefined,
"[project]/src/utils/api.ts [app-client] (ecmascript)", ((__turbopack_context__) => {
"use strict";

// API 게이트웨이를 통한 엔드포인트 설정
__turbopack_context__.s([
    "API_ENDPOINTS",
    ()=>API_ENDPOINTS,
    "addToBasket",
    ()=>addToBasket,
    "cancelOrder",
    ()=>cancelOrder,
    "createOrder",
    ()=>createOrder,
    "deleteFromBasket",
    ()=>deleteFromBasket,
    "deleteMember",
    ()=>deleteMember,
    "deleteUser",
    ()=>deleteUser,
    "getBasketList",
    ()=>getBasketList,
    "getBestItems",
    ()=>getBestItems,
    "getBookDetail",
    ()=>getBookDetail,
    "getDeliveryTracking",
    ()=>getDeliveryTracking,
    "getMembers",
    ()=>getMembers,
    "getOrderDetail",
    ()=>getOrderDetail,
    "getOrderList",
    ()=>getOrderList,
    "getUserByUuid",
    ()=>getUserByUuid,
    "getUsers",
    ()=>getUsers,
    "joinMember",
    ()=>joinMember,
    "login",
    ()=>login,
    "logout",
    ()=>logout,
    "modifyUserInfo",
    ()=>modifyUserInfo,
    "reissueToken",
    ()=>reissueToken,
    "searchBooks",
    ()=>searchBooks,
    "signUp",
    ()=>signUp
]);
const API_BASE_URL = 'http://localhost:8000';
const API_ENDPOINTS = {
    // auth-service API 엔드포인트
    AUTH: {
        SIGNUP: "".concat(API_BASE_URL, "/auth-service/api/join"),
        LOGIN: "".concat(API_BASE_URL, "/auth-service/api/login"),
        REISSUE: "".concat(API_BASE_URL, "/auth-service/api/reissue"),
        LOGOUT: "".concat(API_BASE_URL, "/auth-service/api/logout"),
        USERS: "".concat(API_BASE_URL, "/auth-service/api/users"),
        VALIDATE: "".concat(API_BASE_URL, "/auth-service/api/validate")
    },
    // member-service API 엔드포인트
    MEMBER: {
        MEMBERS: "".concat(API_BASE_URL, "/member-service/members"),
        JOIN: "".concat(API_BASE_URL, "/member-service/join"),
        DELETE: "".concat(API_BASE_URL, "/member-service/delete"),
        ORDER_LIST: "".concat(API_BASE_URL, "/member-service/order/list"),
        ORDER_DETAIL: "".concat(API_BASE_URL, "/member-service/order/detail"),
        DELIVERY_TRACKING: "".concat(API_BASE_URL, "/member-service/delivery/tracking"),
        BASKET_ADD: "".concat(API_BASE_URL, "/member-service/basket/addItem"),
        BASKET_DELETE: "".concat(API_BASE_URL, "/member-service/basket/deleteItem"),
        BASKET_LIST: "".concat(API_BASE_URL, "/member-service/basket/list")
    },
    // item-service API 엔드포인트
    ITEM: {
        BEST_ITEMS: "".concat(API_BASE_URL, "/item-service/bestitems"),
        BOOK_DETAIL: "".concat(API_BASE_URL, "/item-service/book/detail"),
        BOOK_BASKET: "".concat(API_BASE_URL, "/item-service/book/basket"),
        ORDER_ITEMS: "".concat(API_BASE_URL, "/item-service/orderitems"),
        BOOK_SEARCH: "".concat(API_BASE_URL, "/item-service/book/search"),
        BOOK_REGISTER: "".concat(API_BASE_URL, "/item-service/book/register"),
        DELETE: "".concat(API_BASE_URL, "/item-service/delete"),
        ITEMS_SIMPLE: "".concat(API_BASE_URL, "/item-service/items/simple")
    },
    // order-service API 엔드포인트
    ORDER: {
        ORDERS: "".concat(API_BASE_URL, "/order-service/orders"),
        CANCEL: "".concat(API_BASE_URL, "/order-service/cancel"),
        LIST: "".concat(API_BASE_URL, "/order-service/list"),
        DETAIL: "".concat(API_BASE_URL, "/order-service/detail"),
        DETAIL_ASYNC: "".concat(API_BASE_URL, "/order-service/detail/async"),
        DELIVERY_TRACKING: "".concat(API_BASE_URL, "/order-service/delivery/tracking"),
        DELIVERY_STATUS: "".concat(API_BASE_URL, "/order-service/delivery/status")
    }
};
// 기본 API 호출 함수
async function apiCall(url) {
    let method = arguments.length > 1 && arguments[1] !== void 0 ? arguments[1] : 'GET', data = arguments.length > 2 ? arguments[2] : void 0, headers = arguments.length > 3 ? arguments[3] : void 0, requireAuth = arguments.length > 4 && arguments[4] !== void 0 ? arguments[4] : false;
    try {
        const requestHeaders = {
            'Content-Type': 'application/json',
            ...headers
        };
        // 인증이 필요한 요청인 경우 JWT 토큰 추가
        if (requireAuth && "object" !== 'undefined') {
            const token = localStorage.getItem('token');
            const refreshToken = localStorage.getItem('refreshToken');
            if (token) {
                requestHeaders['Authorization'] = token.startsWith('Bearer ') ? token : "Bearer ".concat(token);
            }
            if (refreshToken) {
                requestHeaders['refreshToken'] = refreshToken;
            }
        }
        const requestOptions = {
            method,
            headers: requestHeaders
        };
        // POST, PUT, PATCH 요청의 경우 body 추가
        if (data && [
            'POST',
            'PUT',
            'PATCH'
        ].includes(method)) {
            requestOptions.body = JSON.stringify(data);
        }
        const response = await fetch(url, requestOptions);
        console.log('response', response);
        // 응답이 비어있는지 확인
        const responseText = await response.text();
        console.log('responseText', responseText);
        let responseData;
        try {
            // 응답이 JSON인지 확인하고 파싱
            responseData = responseText ? JSON.parse(responseText) : {};
        } catch (parseError) {
            console.error('JSON 파싱 에러:', parseError);
            // JSON이 아닌 경우 빈 객체로 처리
            responseData = {};
        }
        if (response.ok) {
            return {
                success: true,
                data: responseData
            };
        } else {
            return {
                success: false,
                error: responseData.message || "요청이 실패했습니다. (".concat(response.status, ")")
            };
        }
    } catch (error) {
        console.error('API 호출 에러:', error);
        return {
            success: false,
            error: '네트워크 오류가 발생했습니다.'
        };
    }
}
async function signUp(userData) {
    return apiCall(API_ENDPOINTS.AUTH.SIGNUP, 'POST', userData, {}, false);
}
async function login(credentials) {
    return apiCall(API_ENDPOINTS.AUTH.LOGIN, 'POST', credentials, {}, false);
}
async function reissueToken(refreshToken) {
    return apiCall(API_ENDPOINTS.AUTH.REISSUE, 'POST', {}, {
        refreshToken
    }, true);
}
async function logout(accessToken, refreshToken) {
    return apiCall(API_ENDPOINTS.AUTH.LOGOUT, 'DELETE', {}, {
        Authorization: accessToken,
        refreshToken
    }, false);
}
async function getUsers() {
    return apiCall(API_ENDPOINTS.AUTH.USERS, 'GET', undefined, {}, true);
}
async function getUserByUuid(userUuid, accessToken, refreshToken) {
    const authHeaders = {};
    if (accessToken) {
        authHeaders['Authorization'] = accessToken.startsWith('Bearer ') ? accessToken : "Bearer ".concat(accessToken);
    }
    if (refreshToken) {
        authHeaders['refreshToken'] = refreshToken;
    }
    return apiCall("".concat(API_ENDPOINTS.AUTH.USERS, "/").concat(userUuid), 'GET', undefined, authHeaders, !!accessToken);
}
async function modifyUserInfo(userUuid, password) {
    return apiCall(API_ENDPOINTS.AUTH.USERS, 'PUT', {
        password
    }, {
        userUuid
    }, true);
}
async function deleteUser(userUuid) {
    return apiCall(API_ENDPOINTS.AUTH.USERS, 'DELETE', {}, {
        userUuid
    }, true);
}
async function getMembers() {
    return apiCall(API_ENDPOINTS.MEMBER.MEMBERS, 'GET', undefined, {}, false);
}
async function joinMember(userUuid, username) {
    return apiCall(API_ENDPOINTS.MEMBER.JOIN, 'POST', undefined, {
        userUuid
    }, false);
}
async function deleteMember(userUuid) {
    return apiCall(API_ENDPOINTS.MEMBER.DELETE, 'DELETE', undefined, {
        userUuid
    }, false);
}
async function addToBasket(userUuid, itemUuid, count) {
    return apiCall(API_ENDPOINTS.MEMBER.BASKET_ADD, 'POST', {
        itemUuid,
        count
    }, {
        userUuid
    }, false);
}
async function deleteFromBasket(userUuid, itemUuid) {
    return apiCall("".concat(API_ENDPOINTS.MEMBER.BASKET_DELETE, "?itemUuid=").concat(itemUuid), 'DELETE', undefined, {
        userUuid
    }, false);
}
async function getBasketList(userUuid) {
    return apiCall(API_ENDPOINTS.MEMBER.BASKET_LIST, 'GET', undefined, {
        userUuid
    }, false);
}
async function getBestItems() {
    return apiCall(API_ENDPOINTS.ITEM.BEST_ITEMS, 'GET', undefined, {}, false);
}
async function getBookDetail(itemUuid) {
    return apiCall("".concat(API_ENDPOINTS.ITEM.BOOK_DETAIL, "?itemUuid=").concat(itemUuid), 'GET', undefined, {}, false);
}
async function searchBooks(keywords) {
    return apiCall("".concat(API_ENDPOINTS.ITEM.BOOK_SEARCH, "?keywords=").concat(encodeURIComponent(keywords)), 'GET', undefined, {}, false);
}
async function createOrder(userUuid, orderData) {
    return apiCall(API_ENDPOINTS.ORDER.ORDERS, 'POST', orderData, {
        userUuid
    }, false);
}
async function getOrderList(userUuid) {
    return apiCall(API_ENDPOINTS.ORDER.LIST, 'GET', undefined, {
        userUuid
    }, false);
}
async function getOrderDetail(userUuid, orderUuid) {
    return apiCall("".concat(API_ENDPOINTS.ORDER.DETAIL, "?orderUuid=").concat(orderUuid), 'GET', undefined, {
        userUuid
    }, false);
}
async function cancelOrder(userUuid, orderUuid) {
    return apiCall("".concat(API_ENDPOINTS.ORDER.CANCEL, "?orderUuid=").concat(orderUuid), 'PUT', undefined, {
        userUuid
    }, false);
}
async function getDeliveryTracking(userUuid, orderUuid) {
    return apiCall("".concat(API_ENDPOINTS.ORDER.DELIVERY_TRACKING, "?orderUuid=").concat(orderUuid), 'GET', undefined, {
        userUuid
    }, false);
}
;
if (typeof globalThis.$RefreshHelpers$ === 'object' && globalThis.$RefreshHelpers !== null) {
    __turbopack_context__.k.registerExports(__turbopack_context__.m, globalThis.$RefreshHelpers$);
}
}),
"[project]/src/app/products/page.tsx [app-client] (ecmascript)", ((__turbopack_context__) => {
"use strict";

__turbopack_context__.s([
    "default",
    ()=>ProductsPage
]);
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/node_modules/next/dist/compiled/react/jsx-dev-runtime.js [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$index$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/node_modules/next/dist/compiled/react/index.js [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$useQuery$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/node_modules/@tanstack/react-query/build/modern/useQuery.js [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$useMutation$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/node_modules/@tanstack/react-query/build/modern/useMutation.js [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$QueryClientProvider$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/node_modules/@tanstack/react-query/build/modern/QueryClientProvider.js [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$utils$2f$api$2e$ts__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/src/utils/api.ts [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$lucide$2d$react$2f$dist$2f$esm$2f$icons$2f$shopping$2d$cart$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__$3c$export__default__as__ShoppingCart$3e$__ = __turbopack_context__.i("[project]/node_modules/lucide-react/dist/esm/icons/shopping-cart.js [app-client] (ecmascript) <export default as ShoppingCart>");
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$lucide$2d$react$2f$dist$2f$esm$2f$icons$2f$search$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__$3c$export__default__as__Search$3e$__ = __turbopack_context__.i("[project]/node_modules/lucide-react/dist/esm/icons/search.js [app-client] (ecmascript) <export default as Search>");
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$lucide$2d$react$2f$dist$2f$esm$2f$icons$2f$star$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__$3c$export__default__as__Star$3e$__ = __turbopack_context__.i("[project]/node_modules/lucide-react/dist/esm/icons/star.js [app-client] (ecmascript) <export default as Star>");
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$client$2f$app$2d$dir$2f$link$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/node_modules/next/dist/client/app-dir/link.js [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$contexts$2f$AuthContext$2e$tsx__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/src/contexts/AuthContext.tsx [app-client] (ecmascript)");
;
var _s = __turbopack_context__.k.signature();
'use client';
;
;
;
;
;
;
function ProductsPage() {
    var _searchResults_data, _bestItems_data;
    _s();
    const { isAuthenticated, user } = (0, __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$contexts$2f$AuthContext$2e$tsx__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useAuth"])();
    const queryClient = (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$QueryClientProvider$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useQueryClient"])();
    // 검색 키워드 상태 관리
    const [searchKeyword, setSearchKeyword] = (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$index$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useState"])('');
    const [searchInput, setSearchInput] = (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$index$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useState"])('');
    // 베스트 아이템 목록을 가져오는 쿼리
    const { data: bestItems, isLoading: bestItemsLoading, error: bestItemsError } = (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$useQuery$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useQuery"])({
        queryKey: [
            'bestItems'
        ],
        queryFn: __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$utils$2f$api$2e$ts__$5b$app$2d$client$5d$__$28$ecmascript$29$__["getBestItems"],
        staleTime: 5 * 60 * 1000
    });
    // 검색 결과를 가져오는 쿼리 (검색 키워드가 있을 때만 실행)
    const { data: searchResults, isLoading: searchLoading, error: searchError } = (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$useQuery$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useQuery"])({
        queryKey: [
            'searchBooks',
            searchKeyword
        ],
        queryFn: {
            "ProductsPage.useQuery": ()=>(0, __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$utils$2f$api$2e$ts__$5b$app$2d$client$5d$__$28$ecmascript$29$__["searchBooks"])(searchKeyword)
        }["ProductsPage.useQuery"],
        enabled: !!searchKeyword,
        staleTime: 2 * 60 * 1000
    });
    // 검색 실행 함수
    const handleSearch = (e)=>{
        e.preventDefault();
        setSearchKeyword(searchInput);
    };
    // 검색어 초기화 함수
    const handleClearSearch = ()=>{
        setSearchInput('');
        setSearchKeyword('');
    };
    // 장바구니 추가 mutation
    const addToBasketMutation = (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$useMutation$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useMutation"])({
        mutationFn: {
            "ProductsPage.useMutation[addToBasketMutation]": (param)=>{
                let { itemUuid } = param;
                return (0, __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$utils$2f$api$2e$ts__$5b$app$2d$client$5d$__$28$ecmascript$29$__["addToBasket"])((user === null || user === void 0 ? void 0 : user.userUuid) || '', itemUuid, 1);
            }
        }["ProductsPage.useMutation[addToBasketMutation]"],
        onSuccess: {
            "ProductsPage.useMutation[addToBasketMutation]": ()=>{
                queryClient.invalidateQueries({
                    queryKey: [
                        'basketList'
                    ]
                });
                alert('장바구니에 추가되었습니다.');
            }
        }["ProductsPage.useMutation[addToBasketMutation]"],
        onError: {
            "ProductsPage.useMutation[addToBasketMutation]": ()=>{
                alert('장바구니 추가에 실패했습니다.');
            }
        }["ProductsPage.useMutation[addToBasketMutation]"]
    });
    // 장바구니 담기 핸들러
    const handleAddToCart = (itemUuid)=>{
        if (!isAuthenticated) {
            alert('로그인이 필요합니다.');
            return;
        }
        addToBasketMutation.mutate({
            itemUuid
        });
    };
    return /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
        className: "min-h-screen bg-gray-50",
        children: [
            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("header", {
                className: "bg-white shadow-sm",
                children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                    className: "max-w-7xl mx-auto px-4 sm:px-6 lg:px-8",
                    children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                        className: "flex justify-between items-center py-6",
                        children: [
                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(__TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$client$2f$app$2d$dir$2f$link$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["default"], {
                                href: "/",
                                className: "text-2xl font-bold text-indigo-600",
                                children: "도란 온라인 서가"
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 74,
                                columnNumber: 13
                            }, this),
                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("nav", {
                                className: "flex space-x-8",
                                children: [
                                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(__TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$client$2f$app$2d$dir$2f$link$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["default"], {
                                        href: "/",
                                        className: "text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium",
                                        children: "홈"
                                    }, void 0, false, {
                                        fileName: "[project]/src/app/products/page.tsx",
                                        lineNumber: 78,
                                        columnNumber: 15
                                    }, this),
                                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(__TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$client$2f$app$2d$dir$2f$link$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["default"], {
                                        href: "/products",
                                        className: "text-gray-900 hover:text-indigo-600 px-3 py-2 text-sm font-medium",
                                        children: "상품"
                                    }, void 0, false, {
                                        fileName: "[project]/src/app/products/page.tsx",
                                        lineNumber: 81,
                                        columnNumber: 15
                                    }, this),
                                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(__TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$client$2f$app$2d$dir$2f$link$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["default"], {
                                        href: "/cart",
                                        className: "text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium",
                                        children: "장바구니"
                                    }, void 0, false, {
                                        fileName: "[project]/src/app/products/page.tsx",
                                        lineNumber: 84,
                                        columnNumber: 15
                                    }, this),
                                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(__TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$client$2f$app$2d$dir$2f$link$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["default"], {
                                        href: "/orders",
                                        className: "text-gray-500 hover:text-indigo-600 px-3 py-2 text-sm font-medium",
                                        children: "주문내역"
                                    }, void 0, false, {
                                        fileName: "[project]/src/app/products/page.tsx",
                                        lineNumber: 87,
                                        columnNumber: 15
                                    }, this)
                                ]
                            }, void 0, true, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 77,
                                columnNumber: 13
                            }, this)
                        ]
                    }, void 0, true, {
                        fileName: "[project]/src/app/products/page.tsx",
                        lineNumber: 73,
                        columnNumber: 11
                    }, this)
                }, void 0, false, {
                    fileName: "[project]/src/app/products/page.tsx",
                    lineNumber: 72,
                    columnNumber: 9
                }, this)
            }, void 0, false, {
                fileName: "[project]/src/app/products/page.tsx",
                lineNumber: 71,
                columnNumber: 7
            }, this),
            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("main", {
                className: "max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8",
                children: [
                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                        className: "mb-8",
                        children: [
                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("h1", {
                                className: "text-3xl font-bold text-gray-900 mb-6",
                                children: "상품 목록"
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 99,
                                columnNumber: 11
                            }, this),
                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("form", {
                                onSubmit: handleSearch,
                                className: "max-w-md mx-auto",
                                children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                    className: "flex",
                                    children: [
                                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                            className: "relative flex-1",
                                            children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("input", {
                                                type: "text",
                                                value: searchInput,
                                                onChange: (e)=>setSearchInput(e.target.value),
                                                placeholder: "상품명을 검색하세요...",
                                                className: "w-full px-4 py-2 border border-gray-300 rounded-l-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 text-gray-900"
                                            }, void 0, false, {
                                                fileName: "[project]/src/app/products/page.tsx",
                                                lineNumber: 105,
                                                columnNumber: 17
                                            }, this)
                                        }, void 0, false, {
                                            fileName: "[project]/src/app/products/page.tsx",
                                            lineNumber: 104,
                                            columnNumber: 15
                                        }, this),
                                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("button", {
                                            type: "submit",
                                            className: "px-4 py-2 bg-indigo-600 text-white rounded-r-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2",
                                            children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(__TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$lucide$2d$react$2f$dist$2f$esm$2f$icons$2f$search$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__$3c$export__default__as__Search$3e$__["Search"], {
                                                className: "w-5 h-5"
                                            }, void 0, false, {
                                                fileName: "[project]/src/app/products/page.tsx",
                                                lineNumber: 117,
                                                columnNumber: 17
                                            }, this)
                                        }, void 0, false, {
                                            fileName: "[project]/src/app/products/page.tsx",
                                            lineNumber: 113,
                                            columnNumber: 15
                                        }, this)
                                    ]
                                }, void 0, true, {
                                    fileName: "[project]/src/app/products/page.tsx",
                                    lineNumber: 103,
                                    columnNumber: 13
                                }, this)
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 102,
                                columnNumber: 11
                            }, this),
                            searchKeyword && /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                className: "mt-4 text-center",
                                children: [
                                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("span", {
                                        className: "text-sm text-gray-600",
                                        children: [
                                            '"',
                                            searchKeyword,
                                            '" 검색 결과'
                                        ]
                                    }, void 0, true, {
                                        fileName: "[project]/src/app/products/page.tsx",
                                        lineNumber: 125,
                                        columnNumber: 15
                                    }, this),
                                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("button", {
                                        onClick: handleClearSearch,
                                        className: "ml-2 text-sm text-indigo-600 hover:text-indigo-500",
                                        children: "전체 상품 보기"
                                    }, void 0, false, {
                                        fileName: "[project]/src/app/products/page.tsx",
                                        lineNumber: 128,
                                        columnNumber: 15
                                    }, this)
                                ]
                            }, void 0, true, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 124,
                                columnNumber: 13
                            }, this)
                        ]
                    }, void 0, true, {
                        fileName: "[project]/src/app/products/page.tsx",
                        lineNumber: 98,
                        columnNumber: 9
                    }, this),
                    searchKeyword ? // 검색 결과 표시
                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                        children: searchLoading ? /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                            className: "grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4",
                            children: [
                                ...Array(8)
                            ].map((_, i)=>/*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                    className: "bg-white rounded-lg shadow-md p-6 animate-pulse",
                                    children: [
                                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                            className: "w-full h-48 bg-gray-200 rounded-md mb-4"
                                        }, void 0, false, {
                                            fileName: "[project]/src/app/products/page.tsx",
                                            lineNumber: 146,
                                            columnNumber: 21
                                        }, this),
                                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                            className: "h-4 bg-gray-200 rounded mb-2"
                                        }, void 0, false, {
                                            fileName: "[project]/src/app/products/page.tsx",
                                            lineNumber: 147,
                                            columnNumber: 21
                                        }, this),
                                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                            className: "h-4 bg-gray-200 rounded w-3/4"
                                        }, void 0, false, {
                                            fileName: "[project]/src/app/products/page.tsx",
                                            lineNumber: 148,
                                            columnNumber: 21
                                        }, this)
                                    ]
                                }, i, true, {
                                    fileName: "[project]/src/app/products/page.tsx",
                                    lineNumber: 145,
                                    columnNumber: 19
                                }, this))
                        }, void 0, false, {
                            fileName: "[project]/src/app/products/page.tsx",
                            lineNumber: 143,
                            columnNumber: 15
                        }, this) : searchError ? /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                            className: "text-center py-12",
                            children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("p", {
                                className: "text-red-600",
                                children: "검색 중 오류가 발생했습니다."
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 154,
                                columnNumber: 17
                            }, this)
                        }, void 0, false, {
                            fileName: "[project]/src/app/products/page.tsx",
                            lineNumber: 153,
                            columnNumber: 15
                        }, this) : (searchResults === null || searchResults === void 0 ? void 0 : searchResults.success) && ((_searchResults_data = searchResults.data) === null || _searchResults_data === void 0 ? void 0 : _searchResults_data.length) > 0 ? /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                            className: "grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4",
                            children: searchResults.data.map((item)=>/*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(ProductCard, {
                                    item: item,
                                    onAddToCart: handleAddToCart
                                }, item.itemUuid, false, {
                                    fileName: "[project]/src/app/products/page.tsx",
                                    lineNumber: 159,
                                    columnNumber: 19
                                }, this))
                        }, void 0, false, {
                            fileName: "[project]/src/app/products/page.tsx",
                            lineNumber: 157,
                            columnNumber: 15
                        }, this) : /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                            className: "text-center py-12",
                            children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("p", {
                                className: "text-gray-600",
                                children: "검색 결과가 없습니다."
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 164,
                                columnNumber: 17
                            }, this)
                        }, void 0, false, {
                            fileName: "[project]/src/app/products/page.tsx",
                            lineNumber: 163,
                            columnNumber: 15
                        }, this)
                    }, void 0, false, {
                        fileName: "[project]/src/app/products/page.tsx",
                        lineNumber: 141,
                        columnNumber: 11
                    }, this) : // 베스트 아이템 표시
                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                        children: [
                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("h2", {
                                className: "text-2xl font-bold text-gray-900 mb-6",
                                children: "베스트 상품"
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 171,
                                columnNumber: 13
                            }, this),
                            bestItemsLoading ? /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                className: "grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4",
                                children: [
                                    ...Array(8)
                                ].map((_, i)=>/*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                        className: "bg-white rounded-lg shadow-md p-6 animate-pulse",
                                        children: [
                                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                                className: "w-full h-48 bg-gray-200 rounded-md mb-4"
                                            }, void 0, false, {
                                                fileName: "[project]/src/app/products/page.tsx",
                                                lineNumber: 176,
                                                columnNumber: 21
                                            }, this),
                                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                                className: "h-4 bg-gray-200 rounded mb-2"
                                            }, void 0, false, {
                                                fileName: "[project]/src/app/products/page.tsx",
                                                lineNumber: 177,
                                                columnNumber: 21
                                            }, this),
                                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                                className: "h-4 bg-gray-200 rounded w-3/4"
                                            }, void 0, false, {
                                                fileName: "[project]/src/app/products/page.tsx",
                                                lineNumber: 178,
                                                columnNumber: 21
                                            }, this)
                                        ]
                                    }, i, true, {
                                        fileName: "[project]/src/app/products/page.tsx",
                                        lineNumber: 175,
                                        columnNumber: 19
                                    }, this))
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 173,
                                columnNumber: 15
                            }, this) : bestItemsError ? /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                className: "text-center py-12",
                                children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("p", {
                                    className: "text-red-600",
                                    children: "상품을 불러오는데 실패했습니다."
                                }, void 0, false, {
                                    fileName: "[project]/src/app/products/page.tsx",
                                    lineNumber: 184,
                                    columnNumber: 17
                                }, this)
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 183,
                                columnNumber: 15
                            }, this) : (bestItems === null || bestItems === void 0 ? void 0 : bestItems.success) && ((_bestItems_data = bestItems.data) === null || _bestItems_data === void 0 ? void 0 : _bestItems_data.length) > 0 ? /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                className: "grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-4",
                                children: bestItems.data.map((item)=>/*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(ProductCard, {
                                        item: item,
                                        onAddToCart: handleAddToCart
                                    }, item.itemUuid, false, {
                                        fileName: "[project]/src/app/products/page.tsx",
                                        lineNumber: 189,
                                        columnNumber: 19
                                    }, this))
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 187,
                                columnNumber: 15
                            }, this) : /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                className: "text-center py-12",
                                children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("p", {
                                    className: "text-gray-600",
                                    children: "등록된 상품이 없습니다."
                                }, void 0, false, {
                                    fileName: "[project]/src/app/products/page.tsx",
                                    lineNumber: 194,
                                    columnNumber: 17
                                }, this)
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 193,
                                columnNumber: 15
                            }, this)
                        ]
                    }, void 0, true, {
                        fileName: "[project]/src/app/products/page.tsx",
                        lineNumber: 170,
                        columnNumber: 11
                    }, this)
                ]
            }, void 0, true, {
                fileName: "[project]/src/app/products/page.tsx",
                lineNumber: 96,
                columnNumber: 7
            }, this)
        ]
    }, void 0, true, {
        fileName: "[project]/src/app/products/page.tsx",
        lineNumber: 69,
        columnNumber: 5
    }, this);
}
_s(ProductsPage, "SBHgf8RQqVLFzDUvjlbtmxfkWCM=", false, function() {
    return [
        __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$contexts$2f$AuthContext$2e$tsx__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useAuth"],
        __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$QueryClientProvider$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useQueryClient"],
        __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$useQuery$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useQuery"],
        __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$useQuery$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useQuery"],
        __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f40$tanstack$2f$react$2d$query$2f$build$2f$modern$2f$useMutation$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useMutation"]
    ];
});
_c = ProductsPage;
// 상품 카드 컴포넌트
function ProductCard(param) {
    let { item, onAddToCart } = param;
    var _item_price;
    return /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
        className: "bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow",
        children: [
            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                className: "w-full h-48 bg-gray-200 flex items-center justify-center",
                children: item.itemImageUrl ? /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("img", {
                    src: item.itemImageUrl,
                    alt: item.itemName,
                    className: "w-full h-full object-cover"
                }, void 0, false, {
                    fileName: "[project]/src/app/products/page.tsx",
                    lineNumber: 210,
                    columnNumber: 11
                }, this) : /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(__TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$lucide$2d$react$2f$dist$2f$esm$2f$icons$2f$shopping$2d$cart$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__$3c$export__default__as__ShoppingCart$3e$__["ShoppingCart"], {
                    className: "w-12 h-12 text-gray-400"
                }, void 0, false, {
                    fileName: "[project]/src/app/products/page.tsx",
                    lineNumber: 216,
                    columnNumber: 11
                }, this)
            }, void 0, false, {
                fileName: "[project]/src/app/products/page.tsx",
                lineNumber: 208,
                columnNumber: 7
            }, this),
            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                className: "p-4",
                children: [
                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("h3", {
                        className: "text-lg font-semibold text-gray-900 mb-2 line-clamp-2",
                        children: item.itemName
                    }, void 0, false, {
                        fileName: "[project]/src/app/products/page.tsx",
                        lineNumber: 220,
                        columnNumber: 9
                    }, this),
                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                        className: "flex items-center justify-between mb-2",
                        children: [
                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("span", {
                                className: "text-xl font-bold text-indigo-600",
                                children: [
                                    (_item_price = item.price) === null || _item_price === void 0 ? void 0 : _item_price.toLocaleString(),
                                    "원"
                                ]
                            }, void 0, true, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 224,
                                columnNumber: 11
                            }, this),
                            item.salesCount && /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                className: "flex items-center",
                                children: [
                                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(__TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$lucide$2d$react$2f$dist$2f$esm$2f$icons$2f$star$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__$3c$export__default__as__Star$3e$__["Star"], {
                                        className: "w-4 h-4 text-yellow-400 fill-current"
                                    }, void 0, false, {
                                        fileName: "[project]/src/app/products/page.tsx",
                                        lineNumber: 229,
                                        columnNumber: 15
                                    }, this),
                                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("span", {
                                        className: "text-sm text-gray-600 ml-1",
                                        children: item.salesCount
                                    }, void 0, false, {
                                        fileName: "[project]/src/app/products/page.tsx",
                                        lineNumber: 230,
                                        columnNumber: 15
                                    }, this)
                                ]
                            }, void 0, true, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 228,
                                columnNumber: 13
                            }, this)
                        ]
                    }, void 0, true, {
                        fileName: "[project]/src/app/products/page.tsx",
                        lineNumber: 223,
                        columnNumber: 9
                    }, this),
                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                        className: "flex space-x-2",
                        children: [
                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("button", {
                                onClick: ()=>onAddToCart(item.itemUuid),
                                className: "flex-1 bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 transition-colors text-sm",
                                children: "장바구니"
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 235,
                                columnNumber: 11
                            }, this),
                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])(__TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$client$2f$app$2d$dir$2f$link$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["default"], {
                                href: "/products/".concat(item.itemUuid),
                                className: "flex-1 bg-gray-200 text-gray-700 py-2 px-4 rounded-md hover:bg-gray-300 transition-colors text-center text-sm",
                                children: "상세보기"
                            }, void 0, false, {
                                fileName: "[project]/src/app/products/page.tsx",
                                lineNumber: 241,
                                columnNumber: 11
                            }, this)
                        ]
                    }, void 0, true, {
                        fileName: "[project]/src/app/products/page.tsx",
                        lineNumber: 234,
                        columnNumber: 9
                    }, this)
                ]
            }, void 0, true, {
                fileName: "[project]/src/app/products/page.tsx",
                lineNumber: 219,
                columnNumber: 7
            }, this)
        ]
    }, void 0, true, {
        fileName: "[project]/src/app/products/page.tsx",
        lineNumber: 207,
        columnNumber: 5
    }, this);
}
_c1 = ProductCard;
var _c, _c1;
__turbopack_context__.k.register(_c, "ProductsPage");
__turbopack_context__.k.register(_c1, "ProductCard");
if (typeof globalThis.$RefreshHelpers$ === 'object' && globalThis.$RefreshHelpers !== null) {
    __turbopack_context__.k.registerExports(__turbopack_context__.m, globalThis.$RefreshHelpers$);
}
}),
]);

//# sourceMappingURL=src_6f2578e1._.js.map