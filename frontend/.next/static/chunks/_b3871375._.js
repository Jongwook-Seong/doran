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
"[project]/src/app/login/page.tsx [app-client] (ecmascript)", ((__turbopack_context__) => {
"use strict";

__turbopack_context__.s([
    "default",
    ()=>LoginPage
]);
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/node_modules/next/dist/compiled/react/jsx-dev-runtime.js [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$index$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/node_modules/next/dist/compiled/react/index.js [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$navigation$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/node_modules/next/navigation.js [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$utils$2f$api$2e$ts__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/src/utils/api.ts [app-client] (ecmascript)");
var __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$contexts$2f$AuthContext$2e$tsx__$5b$app$2d$client$5d$__$28$ecmascript$29$__ = __turbopack_context__.i("[project]/src/contexts/AuthContext.tsx [app-client] (ecmascript)");
;
var _s = __turbopack_context__.k.signature();
'use client';
;
;
;
;
function LoginPage() {
    _s();
    // Next.js의 라우터 훅 사용
    const router = (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$navigation$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useRouter"])();
    const { login: authLogin } = (0, __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$contexts$2f$AuthContext$2e$tsx__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useAuth"])();
    // 폼 데이터를 관리하는 상태
    const [formData, setFormData] = (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$index$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useState"])({
        userId: '',
        password: ''
    });
    // 로딩 상태 관리
    const [isLoading, setIsLoading] = (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$index$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useState"])(false);
    // 에러 메시지 상태 관리
    const [errorMessage, setErrorMessage] = (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$index$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useState"])('');
    // 입력 필드 값이 변경될 때 호출되는 함수
    const handleInputChange = (e)=>{
        const { name, value } = e.target;
        setFormData((prev)=>({
                ...prev,
                [name]: value
            }));
    };
    // 폼 제출 처리 함수
    const handleSubmit = async (e)=>{
        e.preventDefault();
        setIsLoading(true);
        setErrorMessage('');
        try {
            // API 유틸리티 함수를 사용하여 로그인 요청
            const result = await (0, __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$utils$2f$api$2e$ts__$5b$app$2d$client$5d$__$28$ecmascript$29$__["login"])(formData);
            if (result.success && result.data) {
                // 로그인 성공 시 AuthContext를 통해 인증 상태 업데이트
                const loginData = result.data;
                console.log('loginData', loginData);
                // 로그인 성공 후 받은 토큰으로 사용자 정보를 조회
                const userResult = await (0, __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$utils$2f$api$2e$ts__$5b$app$2d$client$5d$__$28$ecmascript$29$__["getUserByUuid"])(loginData.userUuid, loginData.accessToken, loginData.refreshToken);
                console.log('userResult', userResult);
                if (userResult.success && userResult.data) {
                    const userData = userResult.data;
                    // AuthContext의 login 함수 호출 (refreshToken도 함께 저장)
                    authLogin({
                        userUuid: loginData.userUuid,
                        userId: userData.userId,
                        username: userData.username,
                        email: userData.email
                    }, loginData.accessToken, loginData.refreshToken);
                    // 성공 메시지 표시 후 메인 페이지로 이동
                    alert('로그인이 완료되었습니다!');
                    router.push('/');
                } else {
                    setErrorMessage('사용자 정보를 가져오는데 실패했습니다.');
                }
            } else {
                // 에러 응답 처리
                setErrorMessage(result.error || '로그인에 실패했습니다.');
            }
        } catch (error) {
            // 네트워크 에러 등 예외 처리
            console.error('로그인 에러:', error);
            setErrorMessage('서버 연결에 실패했습니다. 다시 시도해주세요.');
        } finally{
            setIsLoading(false);
        }
    };
    return /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
        className: "min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8",
        children: [
            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                className: "sm:mx-auto sm:w-full sm:max-w-md",
                children: [
                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("h2", {
                        className: "mt-6 text-center text-3xl font-extrabold text-gray-900",
                        children: "로그인"
                    }, void 0, false, {
                        fileName: "[project]/src/app/login/page.tsx",
                        lineNumber: 97,
                        columnNumber: 9
                    }, this),
                    /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("p", {
                        className: "mt-2 text-center text-sm text-gray-600",
                        children: "도란 온라인 서가에 오신 것을 환영합니다"
                    }, void 0, false, {
                        fileName: "[project]/src/app/login/page.tsx",
                        lineNumber: 100,
                        columnNumber: 9
                    }, this)
                ]
            }, void 0, true, {
                fileName: "[project]/src/app/login/page.tsx",
                lineNumber: 96,
                columnNumber: 7
            }, this),
            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                className: "mt-8 sm:mx-auto sm:w-full sm:max-w-md",
                children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                    className: "bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10",
                    children: [
                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("form", {
                            className: "space-y-6",
                            onSubmit: handleSubmit,
                            children: [
                                /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                    children: [
                                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("label", {
                                            htmlFor: "userId",
                                            className: "block text-sm font-medium text-gray-700",
                                            children: "사용자 ID"
                                        }, void 0, false, {
                                            fileName: "[project]/src/app/login/page.tsx",
                                            lineNumber: 110,
                                            columnNumber: 15
                                        }, this),
                                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                            className: "mt-1",
                                            children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("input", {
                                                id: "userId",
                                                name: "userId",
                                                type: "text",
                                                required: true,
                                                minLength: 3,
                                                value: formData.userId,
                                                onChange: handleInputChange,
                                                className: "appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm text-gray-900",
                                                placeholder: "사용자 ID를 입력해주세요"
                                            }, void 0, false, {
                                                fileName: "[project]/src/app/login/page.tsx",
                                                lineNumber: 114,
                                                columnNumber: 17
                                            }, this)
                                        }, void 0, false, {
                                            fileName: "[project]/src/app/login/page.tsx",
                                            lineNumber: 113,
                                            columnNumber: 15
                                        }, this)
                                    ]
                                }, void 0, true, {
                                    fileName: "[project]/src/app/login/page.tsx",
                                    lineNumber: 109,
                                    columnNumber: 13
                                }, this),
                                /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                    children: [
                                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("label", {
                                            htmlFor: "password",
                                            className: "block text-sm font-medium text-gray-700",
                                            children: "비밀번호"
                                        }, void 0, false, {
                                            fileName: "[project]/src/app/login/page.tsx",
                                            lineNumber: 130,
                                            columnNumber: 15
                                        }, this),
                                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                            className: "mt-1",
                                            children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("input", {
                                                id: "password",
                                                name: "password",
                                                type: "password",
                                                required: true,
                                                minLength: 4,
                                                value: formData.password,
                                                onChange: handleInputChange,
                                                className: "appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm text-gray-900",
                                                placeholder: "비밀번호를 입력해주세요"
                                            }, void 0, false, {
                                                fileName: "[project]/src/app/login/page.tsx",
                                                lineNumber: 134,
                                                columnNumber: 17
                                            }, this)
                                        }, void 0, false, {
                                            fileName: "[project]/src/app/login/page.tsx",
                                            lineNumber: 133,
                                            columnNumber: 15
                                        }, this)
                                    ]
                                }, void 0, true, {
                                    fileName: "[project]/src/app/login/page.tsx",
                                    lineNumber: 129,
                                    columnNumber: 13
                                }, this),
                                errorMessage && /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                    className: "text-red-600 text-sm text-center",
                                    children: errorMessage
                                }, void 0, false, {
                                    fileName: "[project]/src/app/login/page.tsx",
                                    lineNumber: 150,
                                    columnNumber: 15
                                }, this),
                                /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                    children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("button", {
                                        type: "submit",
                                        disabled: isLoading,
                                        className: "w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed",
                                        children: isLoading ? '로그인 중...' : '로그인'
                                    }, void 0, false, {
                                        fileName: "[project]/src/app/login/page.tsx",
                                        lineNumber: 157,
                                        columnNumber: 15
                                    }, this)
                                }, void 0, false, {
                                    fileName: "[project]/src/app/login/page.tsx",
                                    lineNumber: 156,
                                    columnNumber: 13
                                }, this)
                            ]
                        }, void 0, true, {
                            fileName: "[project]/src/app/login/page.tsx",
                            lineNumber: 107,
                            columnNumber: 11
                        }, this),
                        /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                            className: "mt-6",
                            children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                className: "relative",
                                children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("div", {
                                    className: "relative flex justify-center text-sm",
                                    children: /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("span", {
                                        className: "px-2 bg-white text-gray-500",
                                        children: [
                                            "계정이 없으신가요?",
                                            ' ',
                                            /*#__PURE__*/ (0, __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$dist$2f$compiled$2f$react$2f$jsx$2d$dev$2d$runtime$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["jsxDEV"])("a", {
                                                href: "/signup",
                                                className: "font-medium text-indigo-600 hover:text-indigo-500",
                                                children: "회원가입하기"
                                            }, void 0, false, {
                                                fileName: "[project]/src/app/login/page.tsx",
                                                lineNumber: 173,
                                                columnNumber: 19
                                            }, this)
                                        ]
                                    }, void 0, true, {
                                        fileName: "[project]/src/app/login/page.tsx",
                                        lineNumber: 171,
                                        columnNumber: 17
                                    }, this)
                                }, void 0, false, {
                                    fileName: "[project]/src/app/login/page.tsx",
                                    lineNumber: 170,
                                    columnNumber: 15
                                }, this)
                            }, void 0, false, {
                                fileName: "[project]/src/app/login/page.tsx",
                                lineNumber: 169,
                                columnNumber: 13
                            }, this)
                        }, void 0, false, {
                            fileName: "[project]/src/app/login/page.tsx",
                            lineNumber: 168,
                            columnNumber: 11
                        }, this)
                    ]
                }, void 0, true, {
                    fileName: "[project]/src/app/login/page.tsx",
                    lineNumber: 106,
                    columnNumber: 9
                }, this)
            }, void 0, false, {
                fileName: "[project]/src/app/login/page.tsx",
                lineNumber: 105,
                columnNumber: 7
            }, this)
        ]
    }, void 0, true, {
        fileName: "[project]/src/app/login/page.tsx",
        lineNumber: 95,
        columnNumber: 5
    }, this);
}
_s(LoginPage, "MpJT1f+ADG7ixANUy5l0S87psb8=", false, function() {
    return [
        __TURBOPACK__imported__module__$5b$project$5d2f$node_modules$2f$next$2f$navigation$2e$js__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useRouter"],
        __TURBOPACK__imported__module__$5b$project$5d2f$src$2f$contexts$2f$AuthContext$2e$tsx__$5b$app$2d$client$5d$__$28$ecmascript$29$__["useAuth"]
    ];
});
_c = LoginPage;
var _c;
__turbopack_context__.k.register(_c, "LoginPage");
if (typeof globalThis.$RefreshHelpers$ === 'object' && globalThis.$RefreshHelpers !== null) {
    __turbopack_context__.k.registerExports(__turbopack_context__.m, globalThis.$RefreshHelpers$);
}
}),
"[project]/node_modules/next/navigation.js [app-client] (ecmascript)", ((__turbopack_context__, module, exports) => {

module.exports = __turbopack_context__.r("[project]/node_modules/next/dist/client/components/navigation.js [app-client] (ecmascript)");
}),
]);

//# sourceMappingURL=_b3871375._.js.map